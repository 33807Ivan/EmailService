package pl.emailservice.email_service.service

import pl.emailservice.email_service.models.MailboxConfiguration
import org.apache.camel.*
import org.apache.camel.impl.DefaultCamelContext
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private lateinit var messageService: MessageService

    private lateinit var camelContext: CamelContext

    @BeforeEach
    fun setup() {
        camelContext = DefaultCamelContext()
        messageService = MessageService(camelContext)
    }

    @Test
    fun `test configureMailbox with OUTLOOK type`() {
        val configuration = MailboxConfiguration(
            "imap-mail.outlook.com",
            993,
            "user@outlook.com",
            "P@ssw0rd"
        )

        messageService.configureMailbox(configuration)

        val route = camelContext.routes[0]
        val actualUrl = route.endpoint.toString()

        val baseUrl = actualUrl.substringBefore('?')

        assertEquals("imaps://imap-mail.outlook.com:993", baseUrl)
    }
}
