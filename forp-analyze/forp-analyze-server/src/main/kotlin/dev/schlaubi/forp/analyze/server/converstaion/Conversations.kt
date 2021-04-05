package dev.schlaubi.forp.analyze.server.converstaion

import dev.schlaubi.forp.analyze.remote.RemoteConversationEntity
import dev.schlaubi.forp.analyze.remote.RemoteTextInput
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.Input.Companion.toInput
import dev.schlaubi.forp.fetch.input.Input.Companion.toPlainInput
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import java.nio.ByteBuffer

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

fun Route.conversations() {
    location<Conversations> {
        post {
            val conversation = ConversationManager.new()

            context.respond(RemoteConversationEntity(conversation.id))
        }

        location<Conversations.Conversation> {
            put<Conversations.Conversation.Sources> { data ->
                val conversation = ConversationManager.findConversation(data.conversation.id)
                val (plain, text) = context.receive<RemoteTextInput>()
                val input = if (plain) text.toPlainInput() else text.toInput()

                conversation.consumeNewInput(input)

                context.respond(HttpStatusCode.Accepted)
            }

            put<Conversations.Conversation.Files> { data ->
                val conversation = ConversationManager.findConversation(data.conversation.id)
                val parts = call.receiveMultipart()

                var file: ByteReadChannel? = null
                var type: FileInput.FileType? = null
                parts.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "type") {
                                type = FileInput.FileType.valueOf(part.value)
                            }
                        }
                        is PartData.FileItem -> {
                            val reader = ByteBuffer.allocate(part.headers[
                                    HttpHeaders.ContentLength
                            ]!!.toInt())
                            part.provider().readAvailable(reader)
                            file = ByteReadChannel(reader)
                        }

                        else -> TODO()
                    }

                    part.dispose()
                }

                if (file == null || type == null) {
                    TODO()
                }

                conversation.consumeNewInput(file!!.toInput(type!!))

                context.respond(HttpStatusCode.Accepted)
            }
        }
    }
}
