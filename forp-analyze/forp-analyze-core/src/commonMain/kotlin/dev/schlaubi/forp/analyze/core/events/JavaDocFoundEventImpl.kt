package dev.schlaubi.forp.analyze.core.events

import dev.schlaubi.forp.analyze.events.JavaDocFoundEvent
import dev.schlaubi.forp.analyze.javadoc.DocumentedClassObject
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass

internal class JavaDocFoundEventImpl(
    override val exceptionName: QualifiedClass,
    override val doc: DocumentedClassObject,
) : JavaDocFoundEvent
