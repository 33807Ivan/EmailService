package pl.emailservice.email_service.controllers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import reactor.core.publisher.Flux
import org.springframework.http.codec.ServerSentEvent
import pl.emailservice.email_service.models.Email
import pl.emailservice.email_service.service.MessageService

class MessageControllerTest {

    @Test
    fun redirectToIndex_ShouldReturnRedirectView() {

        val messageService = mock(MessageService::class.java)
        val controller = MessageController(messageService)

        val result = controller.redirectToIndex()

        assert(result is String)
        assertEquals("redirect:/index.html", result)
    }

    @Test
    fun streamOutlookEmails_ShouldReturnFluxOfServerSentEvent() {

        val messageService = mock(MessageService::class.java)
        val controller = MessageController(messageService)
        val expectedEvents = Flux.just(ServerSentEvent.builder<Email>().build())

        `when`(messageService.streamOutlookEmails()).thenReturn(expectedEvents)

        val result = controller.streamOutlookEmails()

        assert(result == expectedEvents)
    }
}