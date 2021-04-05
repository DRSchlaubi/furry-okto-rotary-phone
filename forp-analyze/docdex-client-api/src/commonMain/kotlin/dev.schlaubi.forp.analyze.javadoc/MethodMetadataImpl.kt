package dev.schlaubi.forp.analyze.javadoc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MethodMetadataImpl(
    override val owner: String,
    override val parameters: List<MethodParameterImpl>,
    @SerialName("parameter_descriptions")
    override val parameterDescriptions: Map<String, String>,
    override val returns: String,
    @SerialName("returns_description")
    override val returnsDescription: String,
    override val throws: List<ThrowsInfoImpl>
) : MethodMetadata {
    @Serializable
    data class ThrowsInfoImpl(
        @SerialName("key")
        override val exception: String,
        @SerialName("value")
        override val description: String
    ) : MethodMetadata.ThrowsInfo

    @Serializable(with = MethodParameterImpl.Companion::class)
    data class MethodParameterImpl(override val annotations: List<String>, override val type: String, override val name: String) : MethodMetadata.MethodParameter {

        companion object : KSerializer<MethodParameterImpl> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("MethodParameter", PrimitiveKind.STRING)

            override fun serialize(encoder: Encoder, value: MethodParameterImpl) = with(value) {
                encoder.encodeString("${annotations.joinToString(" ")} $type $name")
            }

            override fun deserialize(decoder: Decoder): MethodParameterImpl {
                val input = decoder.decodeString().split(" |\\s".toRegex())
                return if (input.size == 2) {
                    val (type, name) = input
                    MethodParameterImpl(emptyList(), type, name)
                } else {
                    val annotations = input.dropLast(2)
                    val (type, name) = input.takeLast(2)
                    MethodParameterImpl(annotations, type, name)
                }
            }
        }
    }
}
