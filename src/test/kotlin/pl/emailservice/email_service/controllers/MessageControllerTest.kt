package pl.emailservice.email_service.controllers

import pl.emailservice.email_service.models.Email
import pl.emailservice.email_service.service.MessageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.http.ResponseEntity
import java.util.*

class MessageControllerTest {

    @Mock
    private lateinit var messageService: MessageService

    private lateinit var emailApiController: MessageController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        emailApiController = MessageController(messageService)
    }

    @Test
    fun testGetRecentOutlookEmails() {
        val emails = listOf(Email(UUID.randomUUID(), "Subject2", "Content2", emptyList()))
        `when`(messageService.outlookEmails).thenReturn(mutableMapOf<String, Email>().apply {
            emails.forEach { put(it.uuid.toString(), it) }
        })

        val response = emailApiController.getRecentOutlookEmails()

        assertEquals(ResponseEntity.ok(emails), response)
    }
}