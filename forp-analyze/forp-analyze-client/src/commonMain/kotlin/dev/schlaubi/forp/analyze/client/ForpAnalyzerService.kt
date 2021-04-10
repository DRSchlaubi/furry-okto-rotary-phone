package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.remote.RemoteConversationEntity
import dev.schlaubi.forp.analyze.remote.RemoteTextInput
import dev.schlaubi.forp.fetch.input.FileInput
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*

internal class ForpAnalyzerService(private val resources: RestResources) {

    suspend fun createNewConversation(): RemoteConversationEntity =
        resources.httpClient.post(resources.url) {
            url {
                path("/conversations")
            }
        }

    suspend fun uploadPlainText(conversationId: Long, isPlain: Boolean, input: String): Unit =
        resources.httpClient.post {
            conversationUrl(conversationId) {
                path("/sources")
            }

            body = RemoteTextInput(isPlain, input)
        }

    suspend fun uploadFile(
        conversationId: Long,
        input: FileInput,
    ): Unit = resources.httpClient.post {
        conversationUrl(conversationId) {
            path("/files")
        }

        val bytes = input.input.toByteArray()
        val file = ByteReadPacket(bytes)

        val data = formData {
            append("type", input.fileType.name)

            append("file", file)
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
            path(id.toString())
        }

    private fun HttpRequestBuilder.conversationsUrl(block: URLBuilder.() -> Unit) {
        url(URLBuilder(resources.url).apply {
            path("/conversations")
            block()
        }.build())
    }
}
