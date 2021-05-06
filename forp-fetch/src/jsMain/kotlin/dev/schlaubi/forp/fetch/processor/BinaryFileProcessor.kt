package dev.schlaubi.forp.fetch.processor

@OptIn(ExperimentalStdlibApi::class)
public actual fun ByteArray.readToString(): String {
    val chars = map { Char(it.toInt()) }.toCharArray()

    return chars.concatToString()
}
