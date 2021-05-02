# forp-fetch

Forp-fetch allows you to search for stacktraces in hastebin links, images or files

# Contents

- [Download](#download)
- [Docs](#docs)
- [Example](#example)
- [Develop](#develop)

# Download

Module name: `forp-fetch`

Repository information: https://github.com/DRSchlaubi/furry-okto-rotary-phone#download

# Sources

- Strings
- Files
- [Hastebin](https://github.com/seejohnrun/haste-server)
- [Github Gist](https://gist.github.com)
- [Pastebin](https://www.pastebin.com)
- [Ghostbin](https://ghostbin.co)
- [mclo.gs](https://mclo.gs)
- [pasty](https://paste.pelkum.dev)
- Images (Using [GCP Vision](https://cloud.google.com/vision) (JVM only))

# Docs

Docs can be found
here: [fopr.schlau.bi/forp-fetch/forp-fetch](https://fopr.schlau.bi/fopr-fetch/forp-fetch)

# Example

```kotlin
val exception = "https://haste.schlaubi.me/ecamubahom.avrasm"

val fetcher = stackTraceFetcher {
    +HastebinProcessor(defaultHttpClient, "haste.schlaubi.me")
}

val stackTrace = fetcher.fetch(exception.toInput())
```

# Develop

If you want to contribute you have to run this command first

```bash
./gradlew forp-parser:generateKotlinCommonGrammarSource
```
