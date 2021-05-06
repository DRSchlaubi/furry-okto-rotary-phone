import dev.schlaubi.forp.analyze.client.RemoteStackTraceAnalyzer
import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.javadoc.awaitReady
import dev.schlaubi.forp.analyze.on
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.toInput
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
suspend fun main() {
    val client = RemoteStackTraceAnalyzer {
        url("http://localhost:8080")
        authKey = "apple-is-shit"
    }

    client.javadocs.awaitReady()

    println(client.javadocs.storedPackage)

    val convo = client.createNewConversation()
    convo.consumeNewInput(ClassLoader.getSystemResourceAsStream("test2.txt")!!.toInput(FileInput.FileType.PLAIN_TEXT))
    convo.on<Event> { println(this) }

    delay(Duration.minutes(1))
    convo.forget()
    delay(Duration.seconds(10))
    client.close()
    println()
}
