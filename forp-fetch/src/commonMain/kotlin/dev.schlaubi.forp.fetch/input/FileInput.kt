package dev.schlaubi.forp.fetch.input

import io.ktor.utils.io.*

/**
 * Input that represents a file.
 *
 * @property input a [ByteReadChannel] emitting the content of the file
 * @property fileType the approximate [FileType] of the file
 */
public class FileInput(public val input: ByteReadChannel, public val fileType: FileType) : Input {

    /**
     * File types.
     */
    public enum class FileType {
        /**
         * This will trigger the ImageFileProcessor on JVM targets.
         */
        IMAGE,

        /**
         * This will parse the bytes as plain test
         */
        PLAIN_TEXT,

        /**
         * Most processors will parse this as plain test
         */
        BINARY
    }
}
