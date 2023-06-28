package pl.emailservice.email_service.controllers

import pl.emailservice.email_service.service.MessageService
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import pl.emailservice.email_service.models.Email
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api")
class MessageController(private val messageService: MessageService) {

    @GetMapping("/")
    fun redirectToIndex(): String {
        return "redirect:/index.html"
    }

    @GetMapping("/outlook/emails", produces = ["text/event-stream"])
    fun streamOutlookEmails(): Flux<ServerSentEvent<Email>> {
        return messageService.streamOutlookEmails()
    }
}