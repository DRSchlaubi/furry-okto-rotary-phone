package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.SourceFile

internal data class SourceFileImpl(
    override val name: String,
    override val content: String,
    override val potentiallyIncomplete: Boolean
) : SourceFile {

    private val lines by lazy { content.lines() }

    override fun lineAt(lineNumber: Int): String =
        lines.getOrNull(lineNumber - 1) ?: error("File does not have this line")
}
