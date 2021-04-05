package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.parser.stacktrace.RootStackTrace

/**
 * Event signalizing that the processor has found a new [RootStackTrace] in the input.
 */
public interface ExceptionFoundEvent : Event {
    override val type: Event.Type
        get() = Event.Type.EXCEPTION_FOUND

    public val exception: RootStackTrace
}
