package com.pine.lib.net

import android.content.Context

import android.net.wifi.WifiManager
import com.pine.lib.app.c


object NetworkUtils {
    fun getAddressLog(port: Int): String {
        val formattedIpAddress = getIpAddress()
        return "Open http://$formattedIpAddress:$port in your browser"
    }

    fun getIpAddress(): String {
        val wifiManager =
            c().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        return String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
    }
}