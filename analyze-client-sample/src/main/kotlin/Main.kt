import dev.schlaubi.forp.analyze.client.RemoteStackTraceAnalyzerBuilder
import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.javadoc.awaitReady
import dev.schlaubi.forp.analyze.on
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.toInput
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
suspend fun main() {
    val client = RemoteStackTraceAnalyzerBuilder().apply {
        url = Url("http://localhost:8080")
        authKey = "apple-is-shit"
    }.build()

    client.javadocs.awaitReady()

    println(client.javadocs.storedPackage)

    val convo = client.createNewConversation()
    convo.consumeNewInput(ClassLoader.getSystemResourceAsStream("test2.txt")!!.toInput(FileInput.FileType.PLAIN_TEXT))
    convo.on<Event> { println(this) }

    delay(1.minutes)
    convo.forget()
    delay(10.seconds)
    client.close()
}
