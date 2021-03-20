package dev.schlaubi.forp.fetch.processor

import io.ktor.client.*
import io.ktor.http.*

/**
 * Implementation of [PasteServerProcessor] for [pastebin.com](https://www.pastebin.com/)
 */
public class PastebinProcessor(client: HttpClient) : PasteServerProcessor(client) {
    // https://regex101.com/r/N8NBDz/2
    override val regex: Regex =
        """(?:https?://(?:www\.)?)?pastebin\.com/(?:raw/)?(.+?(?=\.|$|/|#))""".toRegex()

    override fun MatchResult.convertToRawUrl(): Url {
        val (key) = destructured

        return URLBuilder("https://www.pastebin.com").apply {
            path("raw", key)
        }.build()
    }
}
