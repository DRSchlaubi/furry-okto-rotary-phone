package dev.schlaubi.forp.analyze.server.converstaion

import dev.schlaubi.forp.analyze.remote.RemoteConversationEntity
import dev.schlaubi.forp.analyze.remote.RemoteTextInput
import dev.schlaubi.forp.analyze.server.auth.forp
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.Input.Companion.toInput
import dev.schlaubi.forp.fetch.input.Input.Companion.toPlainInput
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.writeBytes

@Location("/conversations")
class Conversations {
    @Location("/{id}")
    data class Conversation(val conversations: Conversations, val id: Long) {
        @Location("/files")
        data class Files(val conversation: Conversation)

        @Location("/sources")
        data class Sources(val conversation: Conversation)
    }
}

@OptIn(ExperimentalPathApi::class)
fun Route.conversations() {
    post<Conversations> {
        val (token) = call.forp()
        val conversation = ConversationManager.new(token)

        context.respond(RemoteConversationEntity(conversation.id))
    }

    delete<Conversations.Conversation> { data ->
        val conversation = ConversationManager.findConversation(data.id)

        conversation.forget()

        context.respond(HttpStatusCode.Accepted)
    }

    put<Conversations.Conversation.Sources> { data ->
        val conversation = ConversationManager.findConversation(data.conversation.id)
        val (plain, text) = context.receive<RemoteTextInput>()
        val input = if (plain) text.toPlainInput() else text.toInput()

        conversation.consumeNewInput(input)

        context.respond(HttpStatusCode.Accepted)
    }

    put<Conversations.Conversation.Files> { data ->
        try {
            val conversation = ConversationManager.findConversation(data.conversation.id)
            val parts = call.receiveMultipart()

            var file: ByteReadChannel? = null
            var type: FileInput.FileType? = null
            parts.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "type") {
                            type = FileInput.FileType.valueOf(part.value)
                        } else if (part.name == "file") {
                            println(part.value)
                        }
                    }
                    is PartData.FileItem -> {
                        val bytes = part.provider().readBytes()
                        Path("test.png").writeBytes(bytes)
                        file = ByteReadChannel(bytes)

                    }
                    else -> return@forEachPart context.respond(HttpStatusCode.BadRequest, "Unexpected part")
                }

                part.dispose()
            }

            if (file == null || type == null) {
                return@put context.respond(HttpStatusCode.BadRequest, "Missing file or type")
            }

            conversation.consumeNewInput(file!!.toInput(type!!))

            context.respond(HttpStatusCode.Accepted)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
