package dev.schlaubi.forp.analyze.remote

import dev.schlaubi.forp.analyze.javadoc.DocumentedElementModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

public val ForpModule: SerializersModule =
    EventModule + DocumentedElementModule + StackTraceModule +
            QualifiedClassModule + QualifiedMethodModule + StackTraceElementModule