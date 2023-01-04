package com.pine.lib.provider.server_socket

import android.content.Context
import android.content.res.AssetManager
import java.io.*
import java.net.Socket
import java.net.URLDecoder
import java.util.*


/**
 * Created by amitshekhar on 06/02/17.
 */
class RequestHandler(private val mContext: Context) {
    private val mAssets: AssetManager
    private var mSelectedDatabase: String? = null

    private var responseHandler = HttpResponseHandler()

    init {
        mAssets = mContext.resources.assets
    }

    @Throws(IOException::class)
    fun handle(socket: Socket) {
        var reader: BufferedReader? = null
        var output: PrintStream? = null
        val requestData = RequestData()
        val responseData = ResponseData()

        try {
            var route: String? = null

            // Read HTTP headers and parse out the route.
            reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            var line: String?
            var lines: ArrayList<String> = ArrayList()
            do {
                line = reader.readLine()
                if (line == null) break
                if (line.length === 0) break
                lines.add(line!!)

            } while (true)

            var allData = lines.joinToString("\n").trim()

            val requestMethod = Regex("^(GET|POST|PUT)").find(allData)?.value
            val path = Regex("^\\w+\\s(/[^\\s^\\?^#]*)").find(allData)?.groupValues?.get(1)
            val params = Regex("([^&=?]+)=([^&=\\s]+)").findAll(lines[0])
                .map { it.groupValues[1] to it.groupValues[2] }.toList()
            val contentLength =
                Regex("Content-Length: (\\d+)").find(allData)?.groupValues?.get(1)?.toInt() ?: 0

            if (contentLength > 0) {
                // 解析 POST 数据
                val data = CharArray(contentLength)
                reader.read(data, 0, contentLength)
                val postData = String(data)

                Regex("([^&=?]+)=([^&=]+)").findAll(postData).forEach {
                     requestData.bodyArgs[it.groupValues[1]] = URLDecoder.decode(it.groupValues[2], Charsets.UTF_8.name())
                }
            }


            requestData.method = requestMethod!!
            requestData.urls = path!!.removePrefix("/").split("/")
            params.forEach {
                requestData.args[it.first] = it.second
            }


            output = PrintStream(socket.getOutputStream())
            if (requestData.urls.size == 1 && requestData.urls[0].isEmpty()) {
                requestData.urls = arrayListOf("index.html")
            }



            responseData.request = requestData
            responseHandler.getResponse(requestData, responseData)

            responseData.writeResponse(output)


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                output?.close()
                reader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun writeServerError(output: PrintStream) {
        output.println("HTTP/1.0 500 Internal Server Error")
        output.flush()
    }


}