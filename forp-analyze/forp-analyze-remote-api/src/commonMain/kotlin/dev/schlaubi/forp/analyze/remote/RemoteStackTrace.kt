package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.parser.stacktrace.CausedStackTrace
import dev.schlaubi.forp.parser.stacktrace.RootStackTrace
import dev.schlaubi.forp.parser.stacktrace.StackTrace
import dev.schlaubi.forp.parser.stacktrace.StackTraceElement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal fun StackTrace.serializable(): RemoteStackTrace {
    val exception = exception.serializable()
    val elements = elements.map(StackTraceElement::serializable)
    return when (this) {
        is CausedStackTrace -> serializable(exception, elements) // CausedStackTrace.serializable()

        is RootStackTrace -> RemoteStackTrace.RemoteRootStackTrace(
            exception,
            message,
            elements,
            children.map { it.serializable(exception, elements) }
        )

        else -> error("Unknown stack trace element $this (${this::class.simpleName})")
    }
}

private fun CausedStackTrace.serializable(
    qualifiedClass: RemoteQualifiedClass, list: List<RemoteStackTraceElement>,
): RemoteStackTrace.RemoteCausedStackTrace = RemoteStackTrace.RemoteCausedStackTrace(
    qualifiedClass, message, list
)

public val StackTraceModule: SerializersModule = SerializersModule {
    contextual(RemoteStackTrace.serializer())
    contextual(RemoteStackTrace.RemoteCausedStackTrace.serializer())
    contextual(RemoteStackTrace.RemoteRootStackTrace.serializer())
}

@Serializable
internal sealed class RemoteStackTrace : StackTrace {
    abstract override val exception: RemoteQualifiedClass
    abstract override val message: String?
    abstract override val elements: List<RemoteStackTraceElement>

    @Serializable
    @SerialName("caused")
    data class RemoteCausedStackTrace(
        override val exception: RemoteQualifiedClass,
        override val message: String?,
        override val elements: List<RemoteStackTraceElement>,
    ) : RemoteStackTrace(), CausedStackTrace {
        override val parent: StackTrace
            get() = throw UnsupportedOperationException("This is not supported in remote state")
    }

    @Serializable
    @SerialName("root")
    data class RemoteRootStackTrace(
        override val exception: RemoteQualifiedClass,
        override val message: String?,
        override val elements: List<RemoteStackTraceElement>,
        override val children: List<RemoteCausedStackTrace>,
    ) : RemoteStackTrace(), RootStackTrace
}
