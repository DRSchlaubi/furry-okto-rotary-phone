package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.analyze.javadoc.ClassMetadata

/**
 * Event signalizing the processor found a javadoc for [exceptionName].
 */
public class JavaDocFoundEvent(public val exceptionName: String, public val doc: ClassMetadata) :
    Event