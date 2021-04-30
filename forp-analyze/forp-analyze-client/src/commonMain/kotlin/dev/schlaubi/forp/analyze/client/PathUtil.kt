package dev.schlaubi.forp.analyze.client

import io.ktor.http.*

internal fun URLBuilder.safePath(vararg components: String) {
    val before = encodedPath
    path(components.asList())
    encodedPath = before + encodedPath
}
