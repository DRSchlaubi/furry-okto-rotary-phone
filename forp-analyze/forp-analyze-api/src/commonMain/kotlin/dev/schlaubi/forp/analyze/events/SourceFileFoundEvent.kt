package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.analyze.SourceFile

/**
 * Event signalizing that the processor has found a new [SourceFile].
 */
public data class SourceFileFoundEvent(public val file: SourceFile) : Event
