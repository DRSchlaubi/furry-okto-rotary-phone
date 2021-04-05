package dev.schlaubi.forp.analyze.server.errors

import dev.schlaubi.forp.analyze.server.converstaion.InvalidConversationException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.Serializable

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
data class Error(val status: HttpStatusCode, val description: String?)
