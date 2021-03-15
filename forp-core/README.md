# forp-core

Forp-core is the actual parser. It adds the basic model structure and parsing APIs.

**Note: ** Core can only parse Strings only containing one exception. If you want to parse multiple
Exceptions in the same String
use [forp-find](https://github.com/DRSchlaubi/furry-okto-rotary-phone/tree/main/forp-find)

# Contents

- [Download](#download)
- [Docs](#docs)
- [Example](#example)
- [Develop](#develop)

# Download

TBD

# Docs

Docs can be found here: [fopr.schlau.bi/forp-core/forp-core](https://fopr.schlau.bi/fopr-core/forp-core)

# Example

```kotlin
val exception = """
org.bukkit.plugin.InvalidPluginException: Cannot find main class `de.near.trollplugin.Troll'
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:66) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
"""

val stackTrace = StackTraceParser.parse(exception)
```

# Develop

If you want to contribute you have to run this command first

```bash
./gradlew forp-core:generateGrammarSource
```
