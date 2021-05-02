package dev.schlaubi.forp.analyze.server.errors

import dev.schlaubi.forp.analyze.server.converstaion.InvalidConversationException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

fun Application.installErrorHandler() {
    install(StatusPages) {
        authentication()
        badRequest()
        notFound()
    }
}

private fun StatusPages.Configuration.notFound() {
    exception<InvalidConversationException> { e ->
        val error = Error(HttpStatusCode.NotFound, e.message)

        context.respondError(error)
    }
}

private fun StatusPages.Configuration.badRequest() {
    exception<IllegalArgumentException> { e ->
        val error = Error(HttpStatusCode.BadRequest, e.message)

        context.respondError(error)
    }
}

private fun StatusPages.Configuration.authentication() {
    exception<AuthenticationFailedException> { e ->
        val error = Error(HttpStatusCode.Unauthorized, e.message)

        context.respondError(error)
    }
}

private suspend fun ApplicationCall.respondError(error: Error) = respond(error.status, error)

@Serializable
data class Error(
    @Serializable(with = HttpStatusCodeSerializer::class)
    val status: HttpStatusCode, val description: String?
)

object HttpStatusCodeSerializer : KSerializer<HttpStatusCode> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("code", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): HttpStatusCode = HttpStatusCode.fromValue(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: HttpStatusCode) = encoder.encodeInt(value.value)

}
