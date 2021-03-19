import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import dev.schlaubi.forp.find.StackTraceFinder

/**
 * Checks [input] for [RootStackTrace]s.
 */
fun checkFileForInputs(input: String, conditioner: List<RootStackTrace>.() -> Unit) {
    val stackTrace =
        StackTraceFinder.findStackTraces(input)

    stackTrace.conditioner()
}
