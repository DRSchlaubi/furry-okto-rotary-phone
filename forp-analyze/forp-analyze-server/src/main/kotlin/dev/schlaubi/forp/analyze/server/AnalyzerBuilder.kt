package dev.schlaubi.forp.analyze.server

import com.google.cloud.vision.v1.ImageAnnotatorClient
import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.core.stackTraceAnalyzer
import dev.schlaubi.forp.analyze.javadoc.useDocDex
import dev.schlaubi.forp.analyze.server.config.Config
import dev.schlaubi.forp.fetch.processor.*

fun Config.buildAnalyzer(): StackTraceAnalyzer = stackTraceAnalyzer {
    useDocDex(null, docs.docdexUrl)
    fetcher {
        if (inputs.strings) {
            +PlainStringProcessor()
        }

        if (inputs.files) {
            +BinaryFileProcessor()
        }

        if (inputs.githubGist) {
            addHttpFetcher {
                GithubGistProcessor(it)
            }
        }

        if (inputs.ghostbin) {
            addHttpFetcher {
                GhostbinProcessor(it)
            }
        }

        if (inputs.pastebin) {
            addHttpFetcher {
                PastebinProcessor(it)
            }
        }

        if (!inputs.hastebinUrls.isNullOrEmpty()) {
            addHttpFetcher {
                HastebinProcessor(
                    it,
                    inputs.hastebinUrls!!
                )
            }
        }

        if (inputs.images) {
            +ImageFileProcessor(ImageAnnotatorClient.create())
        }
    }
}
