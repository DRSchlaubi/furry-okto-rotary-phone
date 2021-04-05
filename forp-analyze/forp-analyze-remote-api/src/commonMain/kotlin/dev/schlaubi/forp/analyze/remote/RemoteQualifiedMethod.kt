package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.parser.stacktrace.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

/**
 * Converts the implementation of this [QualifiedMethod] to an implementation which is [Serializable].
 */
internal fun QualifiedMethod.serializable(): RemoteQualifiedMethod {
    val clazz = clazz.serializable()
    return when (this) {
        is InvalidQualifiedMethod -> RemoteQualifiedMethod.RemoteInvalidQualifiedMethod(clazz)
        is DefaultQualifiedMethod -> RemoteQualifiedMethod.RemoteDefaultQualifiedMethod(
            clazz,
            methodName
        )
        is QualifiedConstructor -> RemoteQualifiedMethod.RemoteQualifiedConstructor(clazz)
        is Lambda -> RemoteQualifiedMethod.RemoteLambda(clazz, name)
        else -> error("Unknown qualified method type: $this (${this::class.simpleName}")
    }
}

internal val QualifiedMethodModule = SerializersModule {
    contextual(RemoteQualifiedMethod.serializer())
    contextual(RemoteQualifiedMethod.RemoteInvalidQualifiedMethod.serializer())
    contextual(RemoteQualifiedMethod.RemoteDefaultQualifiedMethod.serializer())
    contextual(RemoteQualifiedMethod.RemoteQualifiedConstructor.serializer())
    contextual(RemoteQualifiedMethod.RemoteLambda.serializer())
}

@Serializable
internal sealed class RemoteQualifiedMethod : QualifiedMethod {
    abstract override val `class`: RemoteQualifiedClass

    @SerialName("invalid")
    @Serializable
    data class RemoteInvalidQualifiedMethod(override val `class`: RemoteQualifiedClass) :
        RemoteQualifiedMethod(), InvalidQualifiedMethod

    @SerialName("default")
    @Serializable
    data class RemoteDefaultQualifiedMethod(
        override val `class`: RemoteQualifiedClass,
        override val methodName: String,
    ) : RemoteQualifiedMethod(), DefaultQualifiedMethod

    @SerialName("constructor")
    @Serializable
    data class RemoteQualifiedConstructor(
        override val `class`: RemoteQualifiedClass,
    ) : RemoteQualifiedMethod(), QualifiedConstructor

    @SerialName("lambda")
    @Serializable
    data class RemoteLambda(
        override val `class`: RemoteQualifiedClass, override val name: String,
    ) : RemoteQualifiedMethod(), Lambda
}
