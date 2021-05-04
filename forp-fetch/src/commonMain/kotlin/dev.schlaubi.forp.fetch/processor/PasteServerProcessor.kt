package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.input.StringInput
import dev.schlaubi.forp.fetch.utils.asyncMap
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Abstract helper implementation of [MultiInputProcessor] for paste servers like hastebin-
 */
public abstract class PasteServerProcessor(private val client: HttpClient) :
    MultiInputProcessor<StringInput, Url> {
    /**
     * A [Regex] which can find URLs to the choosen paste server in a string.
     */
    protected abstract val regex: Regex

    /**
     * Helper method which can convert the Regex [MatchResult] to a [Url] providing the input
     * in plain text.
     */
    protected abstract fun MatchResult.convertToRawUrl(): Url

    override fun supports(input: Input): Boolean = input is StringInput

    override fun processInput(input: StringInput): List<Url> {
        val x = regex.findAll(input.input).map {
            it.convertToRawUrl()
        }.toList()

        return x
    }

    override suspend fun fetchInput(data: List<Url>): List<String> =
        data.asyncMap { client.get(it) }
}
