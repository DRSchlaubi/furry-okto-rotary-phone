package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.Input
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

internal expect fun ByteArray.readToString(): String

/**
 * Implementation of [InputProcessor] which reads the bytes and converts them into a string
 */
public class BinaryFileProcessor : InputProcessor<FileInput, ByteReadChannel> {
    override fun supports(input: Input): Boolean {
        val type = (input as? FileInput)?.fileType ?: return false

        return type == FileInput.FileType.BINARY || type == FileInput.FileType.PLAIN_TEXT
    }

    override fun processInput(input: FileInput): ByteReadChannel = input.input

    @OptIn(DangerousInternalIoApi::class)
    override suspend fun fetchInput(data: ByteReadChannel): List<String> {
        val input = data.readRemaining().readBytes().readToString()

        return listOf(input)
    }
}