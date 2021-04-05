package dev.schlaubi.forp.analyze.javadoc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DocumentedReferenceSerializer : KSerializer<DocumentedReference> {
    private val packageRegex = "[a-z.0-9_]".toRegex()

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("reference", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DocumentedReference) =
        encoder.encodeString(value.raw)

    override fun deserialize(decoder: Decoder): DocumentedReference {
        val input = decoder.decodeString().replace("@", "")
        val `package` = input.takeWhile { it.toString().matches(packageRegex) }

        // length = lastIndex + 1 => first char after the string
        val rest = input.substring(`package`.length)
        return when {
            rest.contains("#") -> {
                val (className, methodName) = rest.split("#")
                DocumentedReference.Method(input, `package`, className, methodName)
            }
            rest.contains("%") -> {
                val (className, fieldName) = rest.split("%")
                DocumentedReference.Field(input, `package`, className, fieldName)
            }
            else -> DocumentedReference.Class(input, `package`, rest)
        }
    }
}
