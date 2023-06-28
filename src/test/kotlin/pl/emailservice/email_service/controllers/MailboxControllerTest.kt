package pl.emailservice.email_service.controllers

import pl.emailservice.email_service.models.MailboxConfiguration
import pl.emailservice.email_service.service.MessageService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class MailboxControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var messageService: MessageService
    private lateinit var mailboxController: MailboxController

    @BeforeEach
    fun setup() {
        messageService = Mockito.mock(MessageService::class.java)

        mailboxController = MailboxController(messageService)

        mockMvc = MockMvcBuilders.standaloneSetup(mailboxController).build()
    }

    @Test
    fun `should return 200 when configuration is not null`() {
        val configuration = MailboxConfiguration("imap.gmail.com", 993, "user@gmail.com", "P@ssw0rd")

        val json = ObjectMapper().writeValueAsString(configuration)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/mailbox/configure")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `should return 400 when configuration is null`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/mailbox/configure")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}