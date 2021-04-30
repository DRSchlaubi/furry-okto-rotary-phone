package dev.schlaubi.forp.analyze.server.config

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uchuhimo.konf.BaseConfig
import com.uchuhimo.konf.Feature
import com.uchuhimo.konf.source.toml
import io.ktor.http.*

fun Config(): Config {
    val objectMapper = jacksonObjectMapper().apply {
        val module = SimpleModule().apply {
            addDeserializer(Url::class, UrlDeserializer)
        }

        registerModule(module)
    }

    val konfig = BaseConfig(mapper = objectMapper).apply {
        addSpec(ForpConfigSpec)
        enable(Feature.FAIL_ON_UNKNOWN_PATH)
    }.from.toml.file("config.toml")

    return Config(konfig)
}