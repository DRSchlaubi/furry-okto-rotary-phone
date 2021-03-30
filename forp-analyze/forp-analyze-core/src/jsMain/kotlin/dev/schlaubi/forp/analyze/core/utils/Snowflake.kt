package dev.schlaubi.forp.analyze.core.utils

import os.NetworkInterfaceBase
import kotlin.random.Random

internal actual fun createNodeId(maxNodeId: Long): Long {
    var nodeId: Long = try {
        val sb = StringBuilder()
        val networkInterfaces: dynamic =
            os.networkInterfaces()

        for (networkInterface in networkInterfaces) {
            if (networkInterface is Array<*>) {
                for (element in networkInterface) {
                    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
                    val address = element as NetworkInterfaceBase

                    val mac = address.mac.split(":")

                    mac.forEach {
                        sb.append(it.toByte())
                    }
                }
            }
        }

        sb.toString().hashCode().toLong()
    } catch (ex: Exception) {
        Random.nextLong()
    }
    nodeId = nodeId and maxNodeId
    return nodeId
}
