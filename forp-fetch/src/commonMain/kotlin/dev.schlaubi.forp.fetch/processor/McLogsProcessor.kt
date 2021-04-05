package dev.schlaubi.forp.fetch.processor

import io.ktor.client.*
import io.ktor.http.*

/**
 * Processor for files sent over [mclo.gs](https://mclo.gs).
 */
public class McLogsProcessor(client: HttpClient) : PasteServerProcessor(client) {

    // https://regex101.com/r/Je0Nw9/1
    @Suppress("SpellCheckingInspection")
    override val regex: Regex = "(https?://)?mclo\\.gs/(\\w+)".toRegex()

    override fun MatchResult.convertToRawUrl(): Url {
        val (key) = destructured
        return URLBuilder(RAW_ENDPOINT).apply {
            path(key)
        }.build()
    }

    private companion object {
        private val RAW_ENDPOINT = Url("https://api.mclo.gs/1/raw")
    }
}
