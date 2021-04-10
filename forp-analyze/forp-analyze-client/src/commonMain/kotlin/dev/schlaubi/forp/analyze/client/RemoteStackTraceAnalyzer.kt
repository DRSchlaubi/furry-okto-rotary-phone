package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import kotlin.coroutines.CoroutineContext

internal class RemoteStackTraceAnalyzer(
    restResources: RestResources,
    override val coroutineContext: CoroutineContext,
) : StackTraceAnalyzer {
    override val ready: Boolean = true
    override val javadocs: JavaDocCache = RemoteJavaDocCache(restResources)

    val service = ForpAnalyzerService(restResources)
    val websocket = WebsocketClient(restResources)

    override suspend fun createNewConversation(): Conversation {
        val conversationEntity = service.createNewConversation()

        return RemoteConversation(conversationEntity.id, this)
    }
}
