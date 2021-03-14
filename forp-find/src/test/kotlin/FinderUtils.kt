import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import dev.schlaubi.forp.find.StackTraceFinder

/**
 * Checks the content of the file by [fileName] for [RootStackTrace]s.
 */
fun checkFileForInputs(fileName: String, conditioner: List<RootStackTrace>.() -> Unit) =
    readFile(fileName) { file ->
        val stackTrace =
            StackTraceFinder.findStackTraces(file)

        stackTrace.conditioner()
    }
