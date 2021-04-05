package dev.schlaubi.forp.analyze.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RemoteTextInput(
    @SerialName("is_plain_text")
    public val isPlainText: Boolean,
    public val text: String
)
