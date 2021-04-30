package dev.schlaubi.forp.analyze

import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import kotlinx.coroutines.CoroutineScope

/**
 * Tool allowing you to analyze stacktraces.
 */
public interface StackTraceAnalyzer : CoroutineScope {
    /**
     * Whether the analyze is already fully initialized or not.
     */
    public val ready: Boolean

    /**
     * Cache for exception documentation.
     *
     * @see JavaDocCache
     */
    public val javadocs: JavaDocCache

    /**
     * Creates a new [Conversation].
     *
     * @see Conversation
     */
    public suspend fun createNewConversation(): Conversation

    /**
     * Closes all resources required by the analyze.
     */
    public suspend fun close()
}
