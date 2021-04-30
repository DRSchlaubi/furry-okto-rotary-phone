package dev.schlaubi.forp.analyze.server.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import io.ktor.http.*

object UrlDeserializer : JsonDeserializer<Url>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Url = Url(p.valueAsString)
}
