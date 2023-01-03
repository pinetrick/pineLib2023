package com.pine.lib.provider.server_socket

import com.pine.lib.debug.d
import com.pine.lib.debug.e
import com.pine.lib.file.AssetsHelper
import com.pine.lib.provider.opr.*

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

        var file: ByteArray? = null

        if (url.endsWith(".html", true) || url.endsWith(".css", true) || url.endsWith(
                ".js",
                true
            )
        ) {
            var fileString = AssetsHelper.read("html/$url")
            if (fileString != null) {
                do {
                    val replace = fileString!!.indexOf("{{html") //10
                    if (replace == -1) break
                    val end = fileString!!.indexOf("}}", replace) //20
                    val fileName =
                        fileString!!.substring(replace + 2, end).trim() //html/html/a.html
                    val fileDetail = AssetsHelper.read(fileName).also { if (it == null) e("Cannot open file$fileName") }
                        ?: break //...
                    val needReplaceString =
                        fileString!!.substring(replace, end + 2) //{{html/html/a.html}}
                    fileString = fileString!!.replace(needReplaceString, fileDetail!!)
                } while (true)
                responseData.content = fileString
            }
        } else {
            responseData.contentByte = AssetsHelper.readAsByteArray("html/$url").also {
                if (it == null) {
                    responseData.content = "File Not Exit"
                }
            }
        }
    }


    fun db() {
        WebDb().run(requestData, responseData)
    }

    fun crash() {
        Crash().run(requestData, responseData)
    }

    fun file() {
        Files().run(requestData, responseData)
    }

    fun header() {
        Header().run(requestData, responseData)
    }

    fun command() {
        Command().run(requestData, responseData)
    }


}