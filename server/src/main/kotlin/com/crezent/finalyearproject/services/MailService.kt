package com.crezent.finalyearproject.services

import com.crezent.finalyearproject.data.dto.EmailRequest
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.RemoteError
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.server.engine.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MailService {

    suspend fun sendVerificationToken(
        senderEmail: String,
        userEmail: String,
        token: String,
        mailerToken: String,
        templateId: String,
        subject: String,
        client: HttpClient,
        baseUrl: String,
        userFirstName: String
    ): Result<Unit, RemoteError> {

        return Result.Success(Unit)

        val emailRequest = EmailRequest(
            from = EmailRequest.Recipient(
                email = senderEmail,
                name = "Mikail Ramadan",
            ),
            to = listOf(
                EmailRequest.Recipient(
                    email = userEmail,
                    name = userFirstName,
                )
            ),
            subject = subject,
            templatedId = templateId,
            personalization = listOf(
                EmailRequest.CustomPersonalization(
                    email = userEmail,
                    personalizationData = EmailRequest.PersonalizationData.OtpData(
                        supportEmail = "dcresentsol@gmail.com",
                        otp = token
                    )
                )
            )
        )

        val emailRequestToString = Json.encodeToString(emailRequest)
        println(emailRequestToString)
        try {
            val result = sendSingleEmail(
                mailerSendClient = client,
                url = baseUrl,
                token = mailerToken,
                emailRequest = emailRequest
            )

            // we will use safe request later
            if (result.status.value in (200..299)) {
                return Result.Success(Unit)
            } else {
                val statusDescription = result.status.description
                println(statusDescription)
                return Result.Error(error = RemoteError.UnKnownError(statusDescription))
            }

        } catch (e: Error) {
            e.printStackTrace()
            return Result.Error(error = RemoteError.UnKnownError(message = e.message ?: ""))

        }
    }

    private suspend fun sendSingleEmail(
        mailerSendClient: HttpClient,
        url: String,
        token: String,
        emailRequest: EmailRequest
    ): HttpResponse =
        mailerSendClient.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(emailRequest)
        }
}

