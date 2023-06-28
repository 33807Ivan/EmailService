package pl.emailservice.email_service.models

data class Attachment(
    val fileName: String,
    val size: Long,
    val extension: String
)
