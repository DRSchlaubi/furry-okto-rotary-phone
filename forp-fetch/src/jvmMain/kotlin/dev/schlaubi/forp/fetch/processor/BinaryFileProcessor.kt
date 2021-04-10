package dev.schlaubi.forp.fetch.processor

public actual fun ByteArray.readToString(): String = toString(Charsets.UTF_8)
