package pl.emailservice.email_service.models

data class MailboxConfiguration(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
)