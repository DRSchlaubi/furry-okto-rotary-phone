import dev.schlaubi.forp.core.StackTraceParser
import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import kotlin.test.assertEquals

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

/**
 * This takes [input] and applies [conditioner] to it.
 */
fun checkFile(input: String, conditioner: ParsedRootStackTrace.() -> Unit) {
    val stackTrace =
        StackTraceParser.parse(input)

    stackTrace.conditioner()
}

