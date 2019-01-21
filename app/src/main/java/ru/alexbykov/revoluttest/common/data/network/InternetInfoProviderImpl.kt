package ru.alexbykov.revoluttest.common.data.network

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class InternetInfoProviderImpl() :InternetInfoProvider {

    override fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1000
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}