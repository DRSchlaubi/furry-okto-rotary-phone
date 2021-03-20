package dev.schlaubi.forp.fetch.processor

internal actual fun ByteArray.readToString(): String {
    val chars = map(Byte::toChar).toCharArray()

    return chars.concatToString()
}
