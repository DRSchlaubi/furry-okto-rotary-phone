package dev.schlaubi.forp.analyze.core.events

import dev.schlaubi.forp.analyze.events.ExceptionFoundEvent
import dev.schlaubi.forp.parser.stacktrace.RootStackTrace

internal class ExceptionFoundEventImpl(override val exception: RootStackTrace) : ExceptionFoundEvent