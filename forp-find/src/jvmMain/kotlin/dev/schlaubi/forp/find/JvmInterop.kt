package dev.schlaubi.forp.find

import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import java.io.InputStream
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * Decodes the text supplied by the [path] using [charset] and searches for [stack traces][ParsedRootStackTrace].
 */
@JvmOverloads
public fun StackTraceFinder.findStackTraces(
    path: Path,
    charset: Charset = Charsets.UTF_8
): List<ParsedRootStackTrace> =
    FileChannel.open(path, StandardOpenOption.READ).use {
        findStackTraces(it, charset)
    }

/**
 * Decodes the text supplied by the [channel] using [charset] and searches for [stack traces][ParsedRootStackTrace].
 *
 * **Important:** This will close the channel
 */
@JvmOverloads
public fun StackTraceFinder.findStackTraces(
    channel: ReadableByteChannel,
    charset: Charset = Charsets.UTF_8
): List<ParsedRootStackTrace> =
    findStackTraces(Channels.newInputStream(channel), charset)

/**
 * Decodes the text supplied by the [inputStream] using [charset] and searches for [stack traces][ParsedRootStackTrace].
 *
 * **Important:** This will call [InputStream.close]
 */
@JvmOverloads
public fun StackTraceFinder.findStackTraces(
    inputStream: InputStream,
    charset: Charset = Charsets.UTF_8
): List<ParsedRootStackTrace> {
    val text = inputStream.use { it.readAllBytes() }.toString(charset)

    return findStackTraces(text)
}
