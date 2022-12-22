package com.pine.lib.provider.server_socket

import com.pine.lib.debug.d
import com.pine.lib.file.AssetsHelper
import com.pine.lib.provider.opr.Command
import com.pine.lib.provider.opr.Crash
import com.pine.lib.provider.opr.Header
import com.pine.lib.provider.opr.WebDb

class HttpResponseHandler {

    lateinit var requestData: RequestData
    lateinit var responseData: ResponseData

    fun getResponse(requestData: RequestData, responseData: ResponseData) {
        try {
            this.requestData = requestData
            this.responseData = responseData

            val func = this::class.java.getDeclaredMethod(requestData.urls[0])

            func.invoke(this)
        } catch (e: Exception) {
            tryOpenFile()
        }
    }


    private fun tryOpenFile() {
        val url = requestData.urls.joinToString("/")
        d("Open File: $url")
        val file = AssetsHelper.readAsByteArray("html/$url")

        if (file != null) {
            responseData.contentByte = file
        } else {
            responseData.content = "File Not Exit"
        }
    }

    fun index() {
        val file = AssetsHelper.read("html/index.html")
        file?.let { responseData.content = it }
    }

    fun db() {
        WebDb().run(requestData, responseData)
    }

    fun crash() {
        Crash().run(requestData, responseData)
    }

    fun header() {
        Header().run(requestData, responseData)
    }

    fun command() {
        Command().run(requestData, responseData)
    }
}