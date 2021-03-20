package dev.schlaubi.forp.fetch.processor

import io.ktor.client.*
import io.ktor.http.*

/**
 * Implementation of [PasteServerProcessor] optimized for [seejohnrun/haste-server](https://github.com/seejohnrun/haste-server)
 */
public class HastebinProcessor(client: HttpClient, hosts: List<String>) :
    PasteServerProcessor(client) {
    public constructor(client: HttpClient, vararg hosts: String) : this(client, hosts.toList())

    // https://regex101.com/r/xHVfiD/1
    override val regex: Regex =
        "(https?://)?(?:(?:www\\.)?)?(${hosts.joinToString("|")})/(?:raw/)?(.+?(?=\\.|\$|/|#))".toRegex()

    override fun MatchResult.convertToRawUrl(): Url {
        val (protocol, host, key) = destructured
        val safeProtocol = if (protocol.isBlank()) "https://" else protocol
        return URLBuilder(safeProtocol + host).apply {
            path("raw", key)
        }.build()
    }
}