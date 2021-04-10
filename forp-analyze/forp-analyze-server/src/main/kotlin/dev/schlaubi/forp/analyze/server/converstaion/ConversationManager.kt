package dev.schlaubi.forp.analyze.server.converstaion

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.remote.RemoteEvent
import dev.schlaubi.forp.analyze.remote.serializable
import dev.schlaubi.forp.analyze.server.Application
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object ConversationManager {

    private val conversations = mutableMapOf<Long, APIConversation>()

    suspend fun new(token: String): Conversation {
        val new = Application.analyzer.newApiConversation(token)
        conversations[new.id] = new

        return new
    }

    fun findConversation(id: Long) = conversations[id] ?: invalidConversation()

    internal fun forget(conversation: Conversation) {
        conversations.remove(conversation.id)
    }
}

private fun invalidConversation(): Nothing = throw InvalidConversationException()

private suspend fun StackTraceAnalyzer.newApiConversation(token: String): APIConversation =
    APIConversation(token, createNewConversation())

class APIConversation(val token: String, private val parent: Conversation) :
    Conversation by parent {

    private val listener: Job = events.onEach {
        val event = RemoteEvent(id, it.serializable())
        Application.webSocket.reportEvent(this, event)
    }.launchIn(Application)

    override fun forget() {
        parent.forget()
        listener.cancel()
        ConversationManager.forget(this)
    }
}

class InvalidConversationException : RuntimeException()
