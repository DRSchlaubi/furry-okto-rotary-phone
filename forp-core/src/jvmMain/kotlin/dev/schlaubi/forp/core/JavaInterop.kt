@file:JvmName("StackTraceParserUtils")

package dev.schlaubi.forp.analyze.server.forp.core

import dev.schlaubi.forp.core.StackTraceParser
import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import org.antlr.v4.kotlinruntime.CharStreams
import java.io.IOException
import java.io.InputStream
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Path

/**
 * Parses the content of [channel] as a [ParsedRootStackTrace] using [charset].
 *
 * **Important:** [channel] is not going to get closed, you need to close it manually
 */
@JvmOverloads
@Throws(IOException::class)
public fun parse(
    channel: ReadableByteChannel,
    charset: Charset = Charsets.UTF_8
): ParsedRootStackTrace =
    StackTraceParser.parse(CharStreams.fromChannel(channel, charset))

/**
 * Parses the content of [path] as a [ParsedRootStackTrace] using [charset].
 */
@JvmOverloads
@Throws(IOException::class)
public fun parse(path: Path, charset: Charset = Charsets.UTF_8): ParsedRootStackTrace =
    StackTraceParser.parse(path, charset)

/**
 * Parses the content of [input] using [charset] into a [ParsedRootStackTrace].
 *
 * **Important:** This will call [InputStream.close]
 */
@JvmOverloads
@Throws(IOException::class)
public fun parse(
    input: InputStream,
    charset: Charset = Charsets.UTF_8
): ParsedRootStackTrace = StackTraceParser.parse(CharStreams.fromStream(input, charset))

