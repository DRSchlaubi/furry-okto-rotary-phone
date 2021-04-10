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
    contextual(RemoteEventData.serializer())

    // Adding those serializers too makes it possible to find them automatically when serializing
    contextual(RemoteEventData.RemoteExceptionFoundEvent.serializer())
    contextual(RemoteEventData.RemoteJavaDocFoundEvent.serializer())
    contextual(RemoteEventData.RemoteSourceFileFoundEvent.serializer())
}

@Serializable
public data class RemoteEvent(
    public val conversationId: Long,
    public val data: RemoteEventData,
)

public fun Event.serializable(): RemoteEventData = when (this) {
    is ExceptionFoundEvent -> RemoteEventData.RemoteExceptionFoundEvent(exception.serializable() as RemoteStackTrace.RemoteRootStackTrace)
    is JavaDocFoundEvent -> RemoteEventData.RemoteJavaDocFoundEvent(exceptionName.serializable(),
        doc as AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl)
    is SourceFileFoundEvent -> RemoteEventData.RemoteSourceFileFoundEvent(file.serializable())
    else -> error("Unknown event type: $this (${this::class.simpleName}")
}

@Serializable
public sealed class RemoteEventData : Event {

    @Serializable
    internal data class RemoteExceptionFoundEvent(override val exception: RemoteStackTrace.RemoteRootStackTrace) :
        RemoteEventData(), ExceptionFoundEvent

    @Serializable
    internal data class RemoteJavaDocFoundEvent(
        override val exceptionName: RemoteQualifiedClass,
        override val doc: AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl,
    ) : RemoteEventData(), JavaDocFoundEvent

    @Serializable
    public data class RemoteSourceFileFoundEvent(override val file: RemoteSourceFile) :
        RemoteEventData(), SourceFileFoundEvent
}
