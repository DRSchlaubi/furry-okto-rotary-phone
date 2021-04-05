package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

/**
 * Converts the implementation of this [QualifiedClass] to an implementation which is [Serializable].
 */
internal fun QualifiedClass.serializable(): RemoteQualifiedClass = RemoteQualifiedClass(this)

internal val QualifiedClassModule = SerializersModule {
    contextual(RemoteQualifiedClass.serializer())
}

@Serializable
internal data class RemoteQualifiedClass(
    override val packagePath: String?,
    override val className: String,
    override val innerClasses: List<String>,
    override val innerClassName: String?,
) : QualifiedClass {
    constructor(parent: QualifiedClass) : this(
        parent.packagePath,
        parent.className,
        parent.innerClasses,
        parent.innerClassName
    )
}
