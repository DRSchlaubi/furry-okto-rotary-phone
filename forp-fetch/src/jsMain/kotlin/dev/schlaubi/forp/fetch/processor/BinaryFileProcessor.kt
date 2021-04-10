package dev.schlaubi.forp.fetch.processor

public actual fun ByteArray.readToString(): String {
    val chars = map(Byte::toChar).toCharArray()

    return chars.concatToString()
}
