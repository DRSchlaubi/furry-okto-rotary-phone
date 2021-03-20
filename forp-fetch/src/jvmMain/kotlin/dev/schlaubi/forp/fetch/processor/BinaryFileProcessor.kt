package dev.schlaubi.forp.fetch.processor

internal actual fun ByteArray.readToString(): String = toString(Charsets.UTF_8)
