import dev.schlaubi.fopr.core.StackTraceParser
import dev.schlaubi.fopr.core.stacktrace.RootStackTrace
import kotlin.test.assertEquals

operator fun <T> T.invoke(block: T.() -> Unit) = run(block)
infix fun <T> T?.shouldBe(other: T) = assertEquals(other, this)

fun checkFile(fileName: String, conditioner: RootStackTrace.() -> Unit) {
    val file = ClassLoader.getSystemResourceAsStream(fileName) ?: error("Could not find sample")
    val stackTrace =
        StackTraceParser.parse(file.readAllBytes().toString(Charsets.UTF_8))

    stackTrace.apply(conditioner)
}
