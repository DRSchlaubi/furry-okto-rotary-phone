import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import dev.schlaubi.forp.find.StackTraceFinder

/**
 * Checks [input] for [ParsedRootStackTrace]s.
 */
fun checkFileForInputs(input: String, conditioner: List<ParsedRootStackTrace>.() -> Unit) {
    val stackTrace =
        StackTraceFinder.findStackTraces(input)

    stackTrace.conditioner()
}
