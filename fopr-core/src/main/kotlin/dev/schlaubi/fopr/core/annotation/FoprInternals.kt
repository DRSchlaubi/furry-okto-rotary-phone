package dev.schlaubi.fopr.core.annotation

import dev.schlaubi.fopr.core.ParsedElement

/**
 * Marks a FOPR API which is supposed to only be used internally.
 *
 * Normally this affects low-level parser APIs which are undocumented and do not guarantee a stable API
 *
 * @see ParsedElement.context
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
public annotation class FoprInternals
