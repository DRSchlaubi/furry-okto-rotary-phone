package dev.schlaubi.forp.analyze.core.events

import dev.schlaubi.forp.analyze.SourceFile
import dev.schlaubi.forp.analyze.events.SourceFileFoundEvent

internal class SourceFileFoundEventImpl(override val file: SourceFile) : SourceFileFoundEvent
