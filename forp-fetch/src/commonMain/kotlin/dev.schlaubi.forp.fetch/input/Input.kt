package dev.schlaubi.forp.fetch.input

import io.ktor.utils.io.*
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * Representation of a [Input] that could contain stack traces.
 */
// You can safely ignore the big red warning as it's an intellij bug
public interface Input {
    public companion object {
        /**
         * Converts a [String] into an input that is not analyzed any further.
         */
        @JvmStatic
        @JvmName("fromPlainString")
        public fun String.toPlainInput(): Input = PlainStringInput(this)

        /**
         * Converts a [String] into an input that might contain links to other sources that
         * could contain stack traces.
         */
        @JvmStatic
        @JvmName("fromString")
        public fun String.toInput(): Input = StringInput(this)

        /**
         * Converts a [ByteReadChannel] into an input.
         *
         * @param type a [FileInput.FileType] describing the input
         */
        @JvmStatic
        @JvmName("fromFile")
        public fun ByteReadChannel.toInput(type: FileInput.FileType): Input = FileInput(this, type)

        /**
         * Converts a [ByteReadChannel] to an [Input] that is being processed as an image of a
         * stacktrace.
         */
        @JvmStatic
        @JvmName("fromImage")
        public fun ByteReadChannel.toImageInput(): Input = toInput(FileInput.FileType.IMAGE)

        /**
         * Converts a [ByteReadChannel] into an input that is not analyzed any further.
         */
        @JvmStatic
        @JvmName("fromPlainText")
        public fun ByteReadChannel.toPlainTextInput(): Input =
            toInput(FileInput.FileType.PLAIN_TEXT)

        /**
         * Converts a [ByteReadChannel] into an input that is not analyzed any further.
         */
        @JvmStatic
        @JvmName("fromBinary")
        public fun ByteReadChannel.toBinaryInput(): Input = toInput(FileInput.FileType.BINARY)
    }
}
