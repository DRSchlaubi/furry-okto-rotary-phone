@file:JvmName("Inputs")

package dev.schlaubi.forp.fetch.input

import dev.schlaubi.forp.fetch.input.Input.Companion.toInput
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.readBytes

/**
 * Convert's an [InputStream] providing a file of [type] to an [Input].
 */
@OptIn(ExperimentalIoApi::class)
@JvmName("fromInputStream")
public fun InputStream.toInput(type: FileInput.FileType): Input = toByteReadChannel().toInput(type)

/**
 * Converts this [Path] to an [Input] of [type].
 */
@OptIn(ExperimentalPathApi::class)
@JvmName("fromPath")
public fun Path.toInput(type: FileInput.FileType): Input =
    ByteReadChannel(readBytes()).toInput(type)

/**
 * Converts this [Path] to an [Input] of [FileInput.FileType.IMAGE].
 */
@JvmName("fromImagePath")
public fun Path.toImageInput(): Input = toInput(FileInput.FileType.IMAGE)

/**
 * Converts this [Path] to an [Input] of [FileInput.FileType.PLAIN_TEXT].
 */
@JvmName("fromPlainTextPath")
public fun Path.toPlainTextInput(): Input = toInput(FileInput.FileType.PLAIN_TEXT)

/**
 * Converts this [Path] to an [Input] of [FileInput.FileType.BINARY].
 */
@JvmName("fromBinaryPath")
public fun Path.toBinaryInput(): Input = toInput(FileInput.FileType.BINARY)
