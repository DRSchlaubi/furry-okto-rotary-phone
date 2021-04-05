import dev.schlaubi.forp.analyze.remote.ForpModule
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

val json = Json {
    serializersModule = ForpModule
}

inline fun <reified B, reified T : B> test(item: T) {
    val string = json.encodeToString(item)
    val parsedString = json.decodeFromString<B>(string)

    assertEquals(item, parsedString, "Source and serialized must be same")
}