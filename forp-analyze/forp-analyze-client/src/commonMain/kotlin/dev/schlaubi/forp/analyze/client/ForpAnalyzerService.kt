package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.remote.RemoteConversationEntity
import dev.schlaubi.forp.analyze.remote.RemoteTextInput
import dev.schlaubi.forp.fetch.input.FileInput
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*

internal class ForpAnalyzerService(private val resources: RestResources) {

    suspend fun createNewConversation(): RemoteConversationEntity =
        resources.httpClient.post(resources.url) {
            url {
                safePath("conversations")
            }
        }

    suspend fun uploadPlainText(conversationId: Long, isPlain: Boolean, input: String): Unit =
        resources.httpClient.put {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            conversationUrl(conversationId) {
                safePath("sources")
            }

            body = RemoteTextInput(isPlain, input)
        }

    suspend fun uploadFile(
        conversationId: Long,
        input: FileInput,
    ): Unit = resources.httpClient.put {
        conversationUrl(conversationId) {
            safePath("files")
        }

        val bytes = input.input.toByteArray()

        val data = formData {
            append("type", input.fileType.name)

            // The name header needs to be present for ktor to recognize it as a file
            append("file", bytes, headersOf(HttpHeaders.ContentDisposition, "filename=upload.file"))
        }

        body = MultiPartFormDataContent(data)
    }

    suspend fun forget(
        conversationId: Long,
    ): Unit = resources.httpClient.delete {
        conversationUrl(conversationId)
    }

    private fun HttpRequestBuilder.conversationUrl(id: Long, block: URLBuilder.() -> Unit = {}) =
        conversationsUrl {
            safePath(id.toString())
            block()
        }

    private fun HttpRequestBuilder.conversationsUrl(block: URLBuilder.() -> Unit) {
        url(URLBuilder(resources.url).apply {
            safePath("conversations")
            block()
        }.build())
    }
}
