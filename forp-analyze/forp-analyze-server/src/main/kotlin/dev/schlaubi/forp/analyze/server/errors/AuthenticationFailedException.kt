package dev.schlaubi.forp.analyze.server.errors

import io.ktor.auth.*

class AuthenticationFailedException(val reason: AuthenticationFailedCause) :
    RuntimeException(reason.toString())
