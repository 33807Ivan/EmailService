package pl.emailservice.email_service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EmailService

fun main(args: Array<String>) {
    SpringApplication.run(EmailService::class.java, *args)
}