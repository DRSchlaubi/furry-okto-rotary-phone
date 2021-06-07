package dev.schlaubi.forp.analyze.server.config

import com.uchuhimo.konf.Config as Konfig
import dev.schlaubi.forp.analyze.server.config.ForpConfigSpec as Spec

class Config(val config: Konfig) {
    val docs = Docs()
    val inputs = Inputs()
    val auth = Auth()

    inner class Docs {
        val docdexUrl by config.property(Spec.Docs.docdexUrl)
    }
    inner class Inputs {
        val strings by config.property(Spec.Input.strings)
        val files by config.property(Spec.Input.files)
        val githubGist by config.property(Spec.Input.githubGist)
        val pastebin by config.property(Spec.Input.pastebin)
        val ghostbin by config.property(Spec.Input.ghostbin)
        val pasty by config.property(Spec.Input.pasty)
        val mclogs by config.property(Spec.Input.mclogs)
        val hastebinUrls by config.property(Spec.Input.hastebinUrls)
        val images by config.property(Spec.Input.images)
        val pastegg by config.property(Spec.Input.pastegg)
    }

    inner class Auth {
        val keys by config.property(Spec.Auth.keys)
    }
}
