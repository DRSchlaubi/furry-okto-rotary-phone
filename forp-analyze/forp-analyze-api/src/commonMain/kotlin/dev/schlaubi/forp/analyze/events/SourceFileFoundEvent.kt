package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.analyze.SourceFile

/**
 * Event signalizing that the processor has found a new [SourceFile].
 */
public interface SourceFileFoundEvent : Event {
    override val type: Event.Type
        get() = Event.Type.SOURCE_FILE_FOUND

    public val file: SourceFile
}
