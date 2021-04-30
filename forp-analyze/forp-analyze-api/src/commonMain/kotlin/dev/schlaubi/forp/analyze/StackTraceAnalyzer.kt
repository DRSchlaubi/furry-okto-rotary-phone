package dev.schlaubi.forp.analyze

import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import kotlinx.coroutines.CoroutineScope

public interface StackTraceAnalyzer : CoroutineScope {
    public val ready: Boolean
    public val javadocs: JavaDocCache
    public suspend fun createNewConversation(): Conversation

    public suspend fun close()
}
