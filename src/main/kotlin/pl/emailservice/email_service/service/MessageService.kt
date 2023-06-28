package pl.emailservice.email_service.service

import jakarta.mail.Part
import jakarta.mail.internet.MimeMultipart
import jakarta.mail.internet.MimeUtility
import org.apache.camel.*
import org.apache.camel.builder.RouteBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import pl.emailservice.email_service.models.Attachment
import pl.emailservice.email_service.models.Email
import pl.emailservice.email_service.models.MailboxConfiguration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.*
import java.nio.charset.Charset

@Service
class MessageService(@Autowired private val camelContext: CamelContext) {

    private val sink: Sinks.Many<Email> = Sinks.many().multicast().onBackpressureBuffer()
    private val charset: Charset = Charset.forName("UTF-8")

    private fun processMailMessage(exchange: Exchange): Email? {
        val mailMessage = exchange.getIn()
        if (mailMessage != null) {
            val subject = MimeUtility.decodeText(mailMessage.getHeader("Subject", String::class.java))
                .toByteArray(charset).toString(charset)
            val messageId = mailMessage.getHeader("Message-ID", String::class.java)

            val content = mailMessage.getBody(String::class.java)
                .toByteArray(charset).toString(charset)

            val uuid = UUID.nameUUIDFromBytes(messageId.toByteArray())

            val attachments: MutableList<Attachment> = mutableListOf()

            if (mailMessage.getBody() is MimeMultipart) {
                val multipart = mailMessage.getBody() as MimeMultipart
                for (i in 0 until multipart.count) {
                    val bodyPart = multipart.getBodyPart(i)
                    if (Part.ATTACHMENT.equals(bodyPart.disposition, ignoreCase = true)) {
                        val filename = bodyPart.fileName

                        val size = bodyPart.size.toLong()

                        var extension = ""
                        val index = filename.lastIndexOf('.')
                        if (index > 0) {
                            extension = filename.substring(index + 1)
                        }
                        attachments.add(Attachment(filename, size, extension))
                    }
                }
            }
            return Email(uuid, subject, content, attachments)
        }
        return null
    }

    fun streamOutlookEmails(): Flux<ServerSentEvent<Email>> {
        return sink.asFlux().map { ServerSentEvent.builder(it).build() }
    }

    fun configureMailbox(mailboxConfiguration: MailboxConfiguration) {
        val route = object : RouteBuilder() {
            override fun configure() {
                from("imaps://${mailboxConfiguration.host}:${mailboxConfiguration.port}?username=${mailboxConfiguration.user}&password=${mailboxConfiguration.password}&delete=false&folderName=INBOX&fetchSize=-1&unseen=false&mapMailMessage=true&closeFolder=false")
                    .split(body())
                    .process { exchange: Exchange ->
                        val email = processMailMessage(exchange)
                        if (email != null) {
                            sink.tryEmitNext(email)
                        }
                    }
            }
        }
        camelContext.addRoutes(route)
        camelContext.start()
    }
}

