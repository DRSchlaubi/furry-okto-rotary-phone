package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.analyze.javadoc.DocumentedClassObject
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass

/**
 * Event signalizing the processor found a javadoc for [exceptionName].
 */
public interface JavaDocFoundEvent : Event {
    override val type: Event.Type
        get() = Event.Type.JAVADOC_FOUND

    public val exceptionName: QualifiedClass
    public val doc: DocumentedClassObject
}
