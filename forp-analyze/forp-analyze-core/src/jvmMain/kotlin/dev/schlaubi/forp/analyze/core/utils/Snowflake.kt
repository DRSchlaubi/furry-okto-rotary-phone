package dev.schlaubi.forp.analyze.core.utils

import java.net.NetworkInterface
import java.security.SecureRandom
import java.util.*

// Adopted from: https://github.com/callicoder/java-snowflake/blob/master/src/main/java/com/callicoder/snowflake/Snowflake.java#L94-L114
internal actual fun createNodeId(maxNodeId: Long): Long {
    var nodeId: Long = try {
        val sb = StringBuilder()
        val networkInterfaces: Enumeration<NetworkInterface> =
            NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface: NetworkInterface = networkInterfaces.nextElement()
            val mac: ByteArray = networkInterface.hardwareAddress
            for (macPort in mac) {
                sb.append(String.format("%02X", macPort))
            }
        }
        sb.toString().hashCode().toLong()
    } catch (ex: Exception) {
        SecureRandom().nextLong()
    }
    nodeId = nodeId and maxNodeId
    return nodeId
}
