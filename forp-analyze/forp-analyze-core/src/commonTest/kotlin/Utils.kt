import dev.schlaubi.forp.analyze.SourceFile
import dev.schlaubi.forp.analyze.core.utils.ClassFinder
import kotlin.test.assertEquals

fun findClasses(input: String, conditioner: List<SourceFile>.() -> Unit) {
    conditioner(ClassFinder.findClasses(input))
}

// Those methods actually are part of test helper but gradle hates me so it throws runtime errors as
// those don't get added to the class path THX


/**
 * This functions to allow for testing like this.
 * ```kotlin
 * element {
 *  property shouldBe value
 * }
 * ```
 *
 * @see shouldBe
 */
operator fun <T> T.invoke(block: T.() -> Unit) = run(block)

/**
 * This functions to allow for testing like this.
 * ```kotlin
 * property shouldBe value
 * ```
 */
infix fun <T> T?.shouldBe(other: T) = assertEquals(other, this)
