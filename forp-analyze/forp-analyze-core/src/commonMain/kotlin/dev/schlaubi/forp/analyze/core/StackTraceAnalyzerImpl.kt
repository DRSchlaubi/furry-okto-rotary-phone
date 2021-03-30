package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.core.utils.Snowflake
import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import dev.schlaubi.forp.fetch.StackTraceFetcher
import dev.schlaubi.forp.fetch.input.Input
import kotlin.coroutines.CoroutineContext

internal class StackTraceAnalyzerImpl(
    private val fetcher: StackTraceFetcher,
    override val coroutineContext: CoroutineContext
) : StackTraceAnalyzer {

    private val memory = mutableMapOf<Long, Conversation>()

    suspend fun fetch(input: Input) = fetcher.fetch(input)
    override val javadocs: JavaDocCache
        get() = TODO("Not yet implemented")

    override suspend fun createNewConversation(): Conversation {
        val conversation = ConversationImpl(this, Snowflake.nextId())

        memory[conversation.id] = conversation

        return conversation
    }

    fun forget(conversationId: Long) = memory.remove(conversationId) != null
}
