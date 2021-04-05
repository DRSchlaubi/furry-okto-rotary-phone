@file:OptIn(ExperimentalSerializationApi::class)

package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.events.ExceptionFoundEvent
import dev.schlaubi.forp.analyze.events.JavaDocFoundEvent
import dev.schlaubi.forp.analyze.events.SourceFileFoundEvent
import dev.schlaubi.forp.analyze.javadoc.AbstractDocumentedObject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

public val EventModule: SerializersModule = SerializersModule {
    contextual(RemoteEvent.serializer())

    // Adding those serializers too makes it possible to find them automatically when serializing
    contextual(RemoteEvent.RemoteExceptionFoundEvent.serializer())
    contextual(RemoteEvent.RemoteJavaDocFoundEvent.serializer())
    contextual(RemoteEvent.RemoteSourceFileFoundEvent.serializer())
}

@Serializable
public sealed class RemoteEvent : Event {
    /* @Transient */
    override val type: Event.Type
        get() = when (this) {
            is ExceptionFoundEvent -> Event.Type.EXCEPTION_FOUND
            is JavaDocFoundEvent -> Event.Type.SOURCE_FILE_FOUND
            is SourceFileFoundEvent -> Event.Type.JAVADOC_FOUND
            else -> error("Unknown event type: $this")
        }

    @Serializable
    internal data class RemoteExceptionFoundEvent(override val exception: RemoteStackTrace.RemoteRootStackTrace) :
        ExceptionFoundEvent

    @Serializable
    internal data class RemoteJavaDocFoundEvent(
        override val exceptionName: RemoteQualifiedClass,
        override val doc: AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl,
    ) : JavaDocFoundEvent

    @Serializable
    public data class RemoteSourceFileFoundEvent(override val file: RemoteSourceFile) :
        SourceFileFoundEvent
}
