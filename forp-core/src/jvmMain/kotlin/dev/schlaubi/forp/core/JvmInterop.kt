package dev.schlaubi.forp.core

import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import org.antlr.v4.kotlinruntime.CharStreams
import java.io.InputStream
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * Parses the content of [channel] as a [RootStackTrace] using [charset].
 *
 * **Important:** [channel] is not going to get closed, you need to close it manually
 */
public fun StackTraceParser.parse(
    channel: ReadableByteChannel,
    charset: Charset = Charsets.UTF_8
): RootStackTrace =
    parse(CharStreams.fromChannel(channel, charset))

/**
 * Parses the content of [path] as a [RootStackTrace] using [charset].
 */
public fun StackTraceParser.parse(path: Path, charset: Charset = Charsets.UTF_8): RootStackTrace {
    require(Files.isRegularFile(path)) { "File has to be regular file" }
    require(Files.isReadable(path)) { "Unable to read file" }
    return FileChannel.open(path, StandardOpenOption.READ)
        .use { channel -> parse(channel, charset) }
}

/**
 * Parses the content of [input] using [charset] into a [RootStackTrace].
 *
 * **Important:** This will call [InputStream.close]
 */
public fun StackTraceParser.parse(
    input: InputStream,
    charset: Charset = Charsets.UTF_8
): RootStackTrace =
    parse(CharStreams.fromStream(input, charset))
