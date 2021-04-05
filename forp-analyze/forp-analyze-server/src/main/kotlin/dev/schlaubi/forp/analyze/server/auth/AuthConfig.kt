package dev.schlaubi.forp.analyze.server.auth

import dev.schlaubi.forp.analyze.server.Application
import io.ktor.auth.*
import io.ktor.http.auth.*
import io.ktor.routing.*

private const val FORP_AUTHENTICATION_NAME = "FORP"

private class ForpAuthenticationProvider(config: Configuration) :
    AuthenticationProvider(config) {
    class Configuration(name: String) : AuthenticationProvider.Configuration(name)
}

internal fun Authentication.Configuration.forp() {
    val provider =
        ForpAuthenticationProvider(
            ForpAuthenticationProvider.Configuration(
                FORP_AUTHENTICATION_NAME
            )
        )

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { authentication ->
        val header = context.request.parseAuthorizationHeader()
        if (header == null) {
            authentication.error("MISSING_HEADER", AuthenticationFailedCause.NoCredentials)
            return@intercept
        }

        if (header !is HttpAuthHeader.Single || header.authScheme != "Bearer") {
            authentication.error("WRONG_HEADER", AuthenticationFailedCause.NoCredentials)
            return@intercept
        }

        val token = header.blob

        if (token !in Application.config.auth.keys) {
            authentication.error("INVALID_CREDENTIALS",
                AuthenticationFailedCause.InvalidCredentials)
            return@intercept
        }

        authentication.principal(ForpPrincipal(token))
    }

    register(provider)
}

fun Route.authenticated(build: Route.() -> Unit): Route =
    authenticate(FORP_AUTHENTICATION_NAME, build = build)
