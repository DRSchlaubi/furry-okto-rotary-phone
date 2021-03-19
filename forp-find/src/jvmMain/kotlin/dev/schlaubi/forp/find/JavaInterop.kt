@file:JvmName("StackTraceFinderUtils")

package dev.schlaubi.forp.find

import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import java.io.IOException
import java.io.InputStream
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Path

/**
 * Decodes the text supplied by the [path] using [charset] and searches for [stack traces][RootStackTrace].
 */
@JvmOverloads
@Throws(IOException::class)
public fun findStackTraces(
    path: Path,
    charset: Charset = Charsets.UTF_8
): List<RootStackTrace> = StackTraceFinder.findStackTraces(path, charset)

/**
 * Decodes the text supplied by the [channel] using [charset] and searches for [stack traces][RootStackTrace].
 *
 * **Important:** This will close the channel
 */
@JvmOverloads
@Throws(IOException::class)
public fun findStackTraces(
    channel: ReadableByteChannel,
    charset: Charset = Charsets.UTF_8
): List<RootStackTrace> =
    StackTraceFinder.findStackTraces(channel, charset)

/**
 * Decodes the text supplied by the [inputStream] using [charset] and searches for [stack traces][RootStackTrace].
 *
 * **Important:** This will call [InputStream.close]
 */
@JvmOverloads
@Throws(IOException::class)
public fun findStackTraces(
    inputStream: InputStream,
    charset: Charset = Charsets.UTF_8
): List<RootStackTrace> = StackTraceFinder.findStackTraces(inputStream, charset)
