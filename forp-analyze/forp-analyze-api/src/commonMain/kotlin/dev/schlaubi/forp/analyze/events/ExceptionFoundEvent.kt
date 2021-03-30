package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.parser.stacktrace.RootStackTrace

/**
 * Event signalizing that the processor has found a new [RootStackTrace] in the input.
 */
public data class ExceptionFoundEvent(public val exception: RootStackTrace) : Event
