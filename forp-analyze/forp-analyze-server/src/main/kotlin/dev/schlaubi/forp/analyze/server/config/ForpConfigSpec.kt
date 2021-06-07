package dev.schlaubi.forp.analyze.server.config

import com.uchuhimo.konf.ConfigSpec
import io.ktor.http.*

object ForpConfigSpec : ConfigSpec("") {
    object Docs : ConfigSpec("docs") {
        val docdexUrl by required<Url>()
    }

    object Input : ConfigSpec("inputs") {
        val strings by optional(true)
        val files by optional(false)
        val githubGist by optional(true)
        val pastebin by optional(true)
        val ghostbin by optional(false)
        val hastebinUrls by optional<List<String>>(emptyList())
        val pasty by optional(false)
        val mclogs by optional(false)
        val images by optional(false)
        val pastegg by optional(false)
    }

    object Auth : ConfigSpec("auth") {
        val keys by required<List<String>>()
    }
}
