package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import kotlinx.coroutines.launch
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val LOG = KotlinLogging.logger { }

internal class RemoteStackTraceAnalyzer(
    private val restResources: RestResources,
    override val coroutineContext: CoroutineContext,
) : StackTraceAnalyzer {
    override val ready: Boolean = true
    override val javadocs: JavaDocCache = RemoteJavaDocCache(restResources)

    val service = ForpAnalyzerService(restResources)
    val websocket = WebsocketClient(restResources)

    init {
        launch {
            LOG.info { "Initially connecting to websocket ..." }
            websocket.connect()
        }
    }

    override suspend fun createNewConversation(): Conversation {
        val conversationEntity = service.createNewConversation()

        return RemoteConversation(conversationEntity.id, this)
    }

    override suspend fun close() {
        restResources.httpClient.close()
        websocket.close()
    }
}
