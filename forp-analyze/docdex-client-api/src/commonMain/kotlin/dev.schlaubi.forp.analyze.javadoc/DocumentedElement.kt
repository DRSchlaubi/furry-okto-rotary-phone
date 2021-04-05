package dev.schlaubi.forp.analyze.javadoc

import kotlinx.serialization.Serializable

@Serializable
data class DocumentedElement(
    val name: String,
    val `object`: AbstractDocumentedObject,
)
