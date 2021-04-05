package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.parser.stacktrace.DefaultStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.EllipsisStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.InvalidStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.StackTraceElement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal fun StackTraceElement.serializable(): RemoteStackTraceElement {
    return when (this) {
        is InvalidStackTraceElement -> RemoteStackTraceElement.RemoteInvalidStackTraceElement
        is EllipsisStackTraceElement -> RemoteStackTraceElement.RemoteEllipsisStackTraceElement(
            skipped
        )
        is DefaultStackTraceElement -> RemoteStackTraceElement.RemoteDefaultStackTraceElement(
            method.serializable(),
            source.serializable(),
            file?.serializable()
        )
        else -> error("Unknown stack trace element: $this (${this::class.simpleName}")
    }
}

private fun DefaultStackTraceElement.Source.serializable(): RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSource {
    return when (this) {
        is DefaultStackTraceElement.NativeSource ->
            RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSource.NativeSource
        is DefaultStackTraceElement.UnknownSource ->
            RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSource.UnknownSource
        is DefaultStackTraceElement.InvalidSource ->
            RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSource.InvalidSource
        is DefaultStackTraceElement.FileSource ->
            RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSource.FileSource(
                fileName, lineNumber
            )

        else -> error("Unknown source: $this (${this::class.simpleName}")
    }
}

private fun DefaultStackTraceElement.SourceFile.serializable(): RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSourceFile =
    RemoteStackTraceElement.RemoteDefaultStackTraceElement.RemoteSourceFile(file,
        version,
        knowsFile,
        fileIsApproximation)

internal val StackTraceElementModule = SerializersModule {
    contextual(RemoteStackTraceElement.serializer())
    contextual(RemoteStackTraceElement.RemoteInvalidStackTraceElement.serializer())
    contextual(RemoteStackTraceElement.RemoteEllipsisStackTraceElement.serializer())
    contextual(RemoteStackTraceElement.RemoteDefaultStackTraceElement.serializer())
}

@Serializable
internal sealed class RemoteStackTraceElement : StackTraceElement {

    @Serializable
    @SerialName("invalid")
    object RemoteInvalidStackTraceElement : RemoteStackTraceElement(), InvalidStackTraceElement

    @Serializable
    @SerialName("ellipsis")
    data class RemoteEllipsisStackTraceElement(override val skipped: Int) :
        RemoteStackTraceElement(),
        EllipsisStackTraceElement

    @SerialName("default")
    @Serializable
    data class RemoteDefaultStackTraceElement(
        override val method: RemoteQualifiedMethod,
        override val source: RemoteSource,
        override val file: RemoteSourceFile?,
    ) : RemoteStackTraceElement(), DefaultStackTraceElement {

        @Serializable
        sealed class RemoteSource : DefaultStackTraceElement.Source {

            @SerialName("native")
            @Serializable
            object NativeSource : RemoteSource(), DefaultStackTraceElement.NativeSource

            @SerialName("unknown")
            @Serializable
            object UnknownSource : RemoteSource(), DefaultStackTraceElement.UnknownSource

            @SerialName("invalid")
            @Serializable
            object InvalidSource : RemoteSource(), DefaultStackTraceElement.InvalidSource

            @SerialName("file")
            @Serializable
            data class FileSource(override val fileName: String, override val lineNumber: Int) :
                RemoteSource(), DefaultStackTraceElement.FileSource
        }

        @Serializable
        data class RemoteSourceFile(
            override val file: String,
            override val version: String,
            override val knowsFile: Boolean,
            override val fileIsApproximation: Boolean,
        ) : DefaultStackTraceElement.SourceFile
    }
}
