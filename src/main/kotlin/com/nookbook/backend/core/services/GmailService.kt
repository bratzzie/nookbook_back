package com.nookbook.backend.core.services

import com.google.api.services.gmail.Gmail
import com.nookbook.backend.core.services.exceptions.EmailFailedToSendException
import org.apache.commons.codec.binary.Base64
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class GmailService(private val gmail: Gmail) {
    fun sendEmail(toAddress: String, subject: String, content: String) {
        val props = Properties()

        val session = Session.getInstance(props, null)

        val email = MimeMessage(session)

        try {
            email.setFrom(InternetAddress("bichurina.anastasiia@gmail.com"))
            email.setRecipient(Message.RecipientType.TO, InternetAddress(toAddress))
            email.setSubject(subject)
            email.setText(content)

            val buffer = ByteArrayOutputStream()
            email.writeTo(buffer)
            val rawMessageBytes = buffer.toByteArray()
            val encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes)
            var message = com.google.api.services.gmail.model.Message()
            message.setRaw(encodedEmail)
            message = gmail.users().messages().send("me", message).execute()
        } catch (ex: Exception) {
            throw EmailFailedToSendException()
        }
    }
}