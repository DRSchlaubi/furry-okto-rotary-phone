package dev.schlaubi.forp.analyze.server.auth

import dev.schlaubi.forp.analyze.server.errors.AuthenticationFailedException
import io.ktor.application.*
import io.ktor.auth.*

data class ForpPrincipal(val token: String) : Principal

fun ApplicationCall.forp(): ForpPrincipal {
    if (authentication.allFailures.isNotEmpty()) {
        throw AuthenticationFailedException(authentication.allFailures.first())
    }
    return principal()!!
}
