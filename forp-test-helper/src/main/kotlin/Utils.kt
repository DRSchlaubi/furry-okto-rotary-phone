import dev.schlaubi.forp.core.StackTraceParser
import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import java.io.InputStream
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
 * This takes the file by [fileName] from the system resources and applies [conditioner] to it.
 *
 * @see ClassLoader.getSystemResourceAsStream
 */
fun checkFile(fileName: String, conditioner: RootStackTrace.() -> Unit) =
    readFile(fileName) { file ->
        val stackTrace =
            StackTraceParser.parse(file)

        stackTrace.conditioner()
    }

/**
 * This takes the file by [fileName] from the system resources and applies [block] to it.
 *
 * @see ClassLoader.getSystemResourceAsStream
 */
fun readFile(fileName: String, block: (file: InputStream) -> Unit) {
    val file = ClassLoader.getSystemResourceAsStream(fileName) ?: error("Could not find sample")

    block(file)
}
