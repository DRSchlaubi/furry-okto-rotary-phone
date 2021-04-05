package dev.schlaubi.forp.analyze.javadoc

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassMetadataImpl(
    override val extensions: List<@Contextual DocumentedReference>,
    override val implementations: List<@Contextual DocumentedReference>,
    @SerialName("all_implementations")
    override val allImplementations: List<@Contextual DocumentedReference>,
    @SerialName("super_interfaces")
    override val superInterfaces: List<@Contextual DocumentedReference>,
    @SerialName("sub_interfaces")
    override val subInterfaces: List<@Contextual DocumentedReference>,
    @SerialName("sub_classes")
    override val subClasses: List<@Contextual DocumentedReference>,
    @SerialName("implementing_classes")
    override val implementingClasses: List<@Contextual DocumentedReference>,
    override val fields: List<@Contextual DocumentedReference>,
    override val methods: List<@Contextual DocumentedReference>
) : ClassMetadata
