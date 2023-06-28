package pl.emailservice.email_service.controllers

import pl.emailservice.email_service.models.MailboxConfiguration
import pl.emailservice.email_service.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mailbox")
class MailboxController(private val messageService: MessageService) {

    @PostMapping("/configure")
    fun configureMailbox(@RequestBody configuration: MailboxConfiguration?): ResponseEntity<Any> {
        return if (configuration != null) {
            messageService.configureMailbox(configuration)
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}