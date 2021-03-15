package dev.schlaubi.forp.find

import dev.schlaubi.forp.core.StackTraceParser
import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import java.io.InputStream
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.StandardOpenOption

@Suppress("unused", "MemberVisibilityCanBePrivate")
public object StackTraceFinder {
    /**
     * Regex used to find the start of a stack trace.
     */
    // https://regex101.com/r/DMTh17/1
    @JvmStatic
    public val STACK_TRACE_START: Regex =
        "^(?:Exception in thread \".*\")?\\s*?(\\S+?(?<=Exception|Error:))"
            .toRegex(RegexOption.MULTILINE)

    /**
     * Decodes the text supplied by the [path] using [charset] and searches for [stack traces][RootStackTrace].
     */
    @JvmStatic
    @JvmOverloads
    public fun findStackTraces(
        path: Path,
        charset: Charset = Charsets.UTF_8
    ): List<RootStackTrace> =
        FileChannel.open(path, StandardOpenOption.READ).use {
            findStackTraces(it, charset)
        }

    /**
     * Decodes the text supplied by the [channel] using [charset] and searches for [stack traces][RootStackTrace].
     *
     * **Important:** This will close the channel
     */
    @JvmStatic
    @JvmOverloads
    public fun findStackTraces(
        channel: ReadableByteChannel,
        charset: Charset = Charsets.UTF_8
    ): List<RootStackTrace> =
        findStackTraces(Channels.newInputStream(channel), charset)

    /**
     * Decodes the text supplied by the [inputStream] using [charset] and searches for [stack traces][RootStackTrace].
     *
     * **Important:** This will call [InputStream.close]
     */
    @JvmStatic
    @JvmOverloads
    public fun findStackTraces(
        inputStream: InputStream,
        charset: Charset = Charsets.UTF_8
    ): List<RootStackTrace> {
        val text = inputStream.use { it.readAllBytes() }.toString(charset)

        return findStackTraces(text)
    }

    /**
     * Searches for [stack traces][RootStackTrace] in [input].
     */
    public fun findStackTraces(input: String): List<RootStackTrace> {
        val possibleStackTraces = STACK_TRACE_START.findAll(input).toList()

        val stackTraces = ArrayList<RootStackTrace>(possibleStackTraces.size)

        var currentInput = input
        var currentEnd = 0
        var lastStart = 0
        for (start in possibleStackTraces) {
            // We should only start parsing the actual exception,
            // therefore we start at the first group
            val range = start.groups[1]?.range ?: continue
            val currentStart = range.first
            if (currentStart < currentEnd) continue

            // Because we always update the base string the index gets shifted
            currentInput = currentInput.substring(currentStart - lastStart)
            val stackTrace = StackTraceParser.parse(currentInput)
            currentEnd = currentStart + 1 + stackTrace.context.stop.stopIndex
            lastStart = currentStart
            stackTraces.add(stackTrace)
        }

        return stackTraces.toList()
    }
}
