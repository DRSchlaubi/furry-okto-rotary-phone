const val trace1 = """org.bukkit.plugin.InvalidPluginException: Cannot find main class `de.near.trollplugin.Troll'
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

const val trace2 = """
    java.lang.NullPointerException: Cannot invoke "java.lang.String.toString()" because "x" is null
	at Test.main(Test.java:5)
"""

const val trace3 = """
    dev.schlaubi.DiedException: An Exception was thrown
  at Test.main(Test.java:5)
Caused by: java.lang.NullPointerException: Cannot invoke "java.lang.String.toString()" because "x" is null
	at Test.main(Test.java:5)
"""