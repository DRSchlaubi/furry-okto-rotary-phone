import dev.schlaubi.forp.analyze.javadoc.AbstractDocumentedObject
import dev.schlaubi.forp.analyze.javadoc.DocumentedElement
import dev.schlaubi.forp.analyze.remote.*
import dev.schlaubi.forp.core.StackTraceParser
import kotlinx.serialization.decodeFromString
import kotlin.js.JsName
import kotlin.test.Test

class EventSerializerTest {

    @JsName("testExceptionFoundEvent")
    @Test
    fun `test exception found event`() {
        val trace2 =
            """org.bukkit.plugin.InvalidPluginException: Cannot find main class `de.near.trollplugin.Troll'
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:66) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:131) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at org.bukkit.plugin.SimplePluginManager.loadPlugin(SimplePluginManager.java:329) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at org.bukkit.plugin.SimplePluginManager.loadPlugins(SimplePluginManager.java:251) [spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at org.bukkit.craftbukkit.v1_8_R1.CraftServer.loadPlugins(CraftServer.java:291) [spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at net.minecraft.server.v1_8_R1.DedicatedServer.init(DedicatedServer.java:152) [spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at net.minecraft.server.v1_8_R1.MinecraftServer.run(MinecraftServer.java:505) [spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at java.lang.Thread.run(Unknown Source) [?:1.8.0_271]
Caused by: java.lang.ClassNotFoundException: de.near.trollplugin.Troll
        at java.net.URLClassLoader.findClass(Unknown Source) ~[?:1.8.0_271]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:101) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:86) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        at java.lang.ClassLoader.loadClass(Unknown Source) ~[?:1.8.0_271]
        at java.lang.ClassLoader.loadClass(Unknown Source) ~[?:1.8.0_271]
        at java.lang.Class.forName0(Native Method) ~[?:1.8.0_271]
        at java.lang.Class.forName(Unknown Source) ~[?:1.8.0_271]
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:64) ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
        ... 7 more
"""
        val exception = StackTraceParser.parse(trace2).serializable()
        val event = RemoteEventData.RemoteExceptionFoundEvent(exception as RemoteStackTrace.RemoteRootStackTrace)

        test(event)
    }

    @JsName("testSourceFoundEvent")
    @Test
    fun `test source found event`() {
        val sourceFile = RemoteSourceFile("Test.java", "Test", false).serializable()
        val event = RemoteEventData.RemoteSourceFileFoundEvent(sourceFile)

        test(event)
    }

    @JsName("testJDFEevent")
    @Test
    fun `test javadoc found event`() {
        val javadoc = json.decodeFromString<DocumentedElement>(javadocText)

        val event =
            RemoteEventData.RemoteJavaDocFoundEvent(
                RemoteQualifiedClass(
                    "java.lang",
                    "String",
                    emptyList(), null
                ),
                javadoc.`object` as AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl
            )

        test(event)
    }
}
