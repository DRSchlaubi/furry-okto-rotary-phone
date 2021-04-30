package dev.schlaubi.forp.fetch.processor

import io.ktor.client.*
import io.ktor.http.*

public class PastyProcessor(client: HttpClient) : PasteServerProcessor(client) {
    // https://regex101.com/r/uNbpcO/1
    override val regex: Regex = "(?:https?://(?:www\\.)?)?paste\\.pelkum\\.dev/(?:raw/)?(.+?(?=\\.|\$|/|#))".toRegex()

    override fun MatchResult.convertToRawUrl(): Url {
        val (url) = destructured
        return Url("https://paste.pelkum.dev/api/v1/pastes/$url")
    }
}
