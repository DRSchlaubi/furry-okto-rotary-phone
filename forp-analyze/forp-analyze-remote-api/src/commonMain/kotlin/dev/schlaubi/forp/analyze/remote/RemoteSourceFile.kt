package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.analyze.SourceFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal fun SourceFile.serializable(): RemoteSourceFile =
    RemoteSourceFile(name, content, potentiallyIncomplete)

internal val SourceFileModule = SerializersModule {
    contextual(RemoteSourceFile.serializer())
}

@Serializable
public data class RemoteSourceFile(
    override val name: String,
    override val content: String,
    @SerialName("potentially_incomplete")
    override val potentiallyIncomplete: Boolean,
) : SourceFile {
    private val lines by lazy(content::lines)
    override fun lineAt(lineNumber: Int): String =
        lines.getOrNull(lineNumber - 1) ?: error("Could not find line $lineNumber")
}
