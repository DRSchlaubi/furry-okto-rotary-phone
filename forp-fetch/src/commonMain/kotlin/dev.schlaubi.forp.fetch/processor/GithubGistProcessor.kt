package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.input.StringInput
import dev.schlaubi.forp.fetch.utils.asyncMap
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

/**
 * Implementation of [InputProcessor] which searches for [Github Gist](https://gist.github.com) links and fetches them.
 *
 * @param client a [HttpClient] with [JsonFeature] added to communicate with the Github API
 */
public class GithubGistProcessor(private val client: HttpClient) :
    MultiInputProcessor<StringInput, Gist> {

    init {
        require(client.feature(JsonFeature) != null) { "JsonFeature was not installed to HttpClient" }
    }

    private val regex: Regex =
        """(?:https?://)?(gist\.github\.com|gist.githubusercontent.com)/(.+?(?=\.|${'$'}|/))/(.+?(?=\.|${'$'}|/|#))""".toRegex()

    override fun supports(input: Input): Boolean = input is StringInput

    override fun processInput(input: StringInput): List<Gist> {
        return regex.findAll(input.input).map {
            val (host, key) = it.destructured
            if (host == "gist.githubusercontent.com") {
                RawGistFile(it.value)
            } else {
                DefaultGist(key)
            }
        }.toList()
    }

    override suspend fun fetchInput(data: List<Gist>): List<String> =
        data.asyncMap { it.downloadFiles(client) }.flatten()
}

public interface Gist {
    public suspend fun downloadFiles(httpClient: HttpClient): List<String>
}

private class RawGistFile(val url: String) : Gist {
    override suspend fun downloadFiles(httpClient: HttpClient): List<String> =
        listOf(httpClient.get(url))
}

private class DefaultGist(val gistKey: String) : Gist {
    private val endpoint = Url("https://api.github.com/gists")

    override suspend fun downloadFiles(httpClient: HttpClient): List<String> {
        val gist = httpClient.get<GithubGist>(endpoint) {
            url {
                path(gistKey)
            }
        }

        return gist.files.values.asyncMap {
            httpClient.get(it.rawUrl)
        }
    }
}

@Serializable
private class GithubGist(val files: Map<String, File>) {
    @Serializable
    class File(val rawUrl: String)
}
