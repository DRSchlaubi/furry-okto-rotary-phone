package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.input.StringInput
import dev.schlaubi.forp.fetch.utils.asyncMap
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val PASTE_GG_FIND_PASTE = "https://api.paste.gg/v1/pastes/"
private const val PASTE_GG_PAGE = "https://paste.gg/p/"

/**
 * Implementation of [InputProcessor] which searches for [paste.gg](https://paste.gg) links and fetches them.
 *
 * @param client a [HttpClient] with [JsonFeature] added to communicate with the Paste GG API
 */
public class PasteGGProcessor(private val client: HttpClient) : MultiInputProcessor<StringInput, Paste> {

    init {
        require(client.feature(JsonFeature) != null) { "JsonFeature was not installed to HttpClient" }
    }

    // https://regex101.com/r/ibGxeT/1/
    private val regex: Regex =
        """(?:https?://(?:www\.)?)?paste\.gg/p/(.*)/(.*)""".toRegex()

    override fun supports(input: Input): Boolean = input is StringInput

    override fun processInput(input: StringInput): List<Paste> {
        return regex.findAll(input.input).map {
            val (user, paste) = it.destructured

            Paste(user, paste)
        }.toList()
    }

    override suspend fun fetchInput(data: List<Paste>): List<String> =
        data.asyncMap { (user, pasteId) ->
            val paste = client.get<PasteGGResponse>(PASTE_GG_FIND_PASTE) {
                url {
                    path(pasteId)
                }
            }

            paste.result.files.map { (fileId) ->
                client.get<String>(PASTE_GG_PAGE) {
                    url {
                        path(user, pasteId, "files", fileId, "raw")
                    }
                }
            }
        }.flatten()
}

/**
 * Representation of a paste.gg paste.
 *
 * @property user the user who uploaded the paste
 * @property id the id of the paste
 */
public data class Paste(val user: String, val id: String)

@Serializable
private data class PasteGGResponse(
    val status: String,
    val result: Paste
) {
    @Serializable
    data class Paste(
        val id: String,
        val name: String,
        val description: String,
        val visibility: String,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("updated_at")
        val updatedAt: String,
        val files: List<File>
    ) {
        @Serializable
        data class File(
            val id: String,
            val name: String,
            @SerialName("highlight_language")
            val highlightLanguage: String? = null
        )
    }
}
