# forp-core

Forp-find is provides an API to search for multiple stack traces within one input source. Unlike core this can find multiple stacktraces within the same input source

# Contents

- [Download](#download)
- [Docs](#docs)
- [Example](#example)
- [Develop](#develop)

# Download

TBD

# Docs

Docs can be found here: [fopr.schlau.bi/forp-find/forp-find](https://fopr.schlau.bi/fopr-find/forp-find)

# Example

```kotlin
val exception = """
org.bukkit.plugin.InvalidPluginException: Cannot find main class `de.near.trollplugin.Troll'
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:66) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        
org.bukkit.plugin.InvalidPluginException: Cannot find main class `de.near.trollplugin.Troll'
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:66) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]        
"""

val stackTrace: List<RootStackTrace> = StackTraceFinder.find(exception)
```

# Develop

If you want to contribute you have to run this command first

```bash
./gradlew forp-core:generateGrammarSource
```
