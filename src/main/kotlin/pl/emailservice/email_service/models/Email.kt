package pl.emailservice.email_service.models

import java.util.UUID

data class Email(
    val uuid: UUID,
    val subject: String,
    val content: String,
    val attachment: List<Attachment>
)