package dev.schlaubi.forp.fetch.processor

import io.ktor.client.*
import io.ktor.http.*

/**
 * Implementation of [PasteServerProcessor] for [ghostbin.co](https://ghostbin.co/)
 */
public class GhostbinProcessor(client: HttpClient) : PasteServerProcessor(client) {

    // https://regex101.com/r/CyjiKt/2
    override val regex: Regex =
        """(?:https?://)?(?:(?:www\.)?)?(?:ghostbin\.co)/(?:paste/)(.+?(?=\.|${'$'}|/))""".toRegex()

    override fun MatchResult.convertToRawUrl(): Url {
        val (key) = destructured
        return URLBuilder("https://ghostbin.co").apply {
            path(key, "raw")
        }.build()
    }
}
