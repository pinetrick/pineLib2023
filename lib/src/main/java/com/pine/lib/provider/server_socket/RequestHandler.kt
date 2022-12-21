package com.pine.lib.provider.server_socket

import android.content.Context
import android.content.res.AssetManager
import java.io.*
import java.net.Socket
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
            while (reader.readLine().also { line = it } != null && line!!.length !== 0) {
                lines.add(line!!)
            }
            var allData = lines.joinToString("\n").trim()

            val requestMethod = Regex("^(GET|POST|PUT)").find(allData)?.value
            val path = Regex("^\\w+\\s(/[^\\s^\\?^#]*)").find(allData)?.groupValues?.get(1)
            val params = Regex("([^&=?]+)=([^&=\\s]+)").findAll(lines[0])
                .map { it.groupValues[1] to it.groupValues[2] }.toList()
            val contentLength =
                Regex("Content-Length: (\\d+)").find(allData)?.groupValues?.get(1)?.toInt() ?: 0
            val body = lines.takeLast(contentLength).joinToString("\n")
            val bodyParams = Regex("([^&=?]+)=([^&=]+)").findAll(body)
                .map { it.groupValues[1] to it.groupValues[2] }.toList()

            requestData.method = requestMethod!!
            requestData.urls = path!!.removePrefix("/").split("/")
            params.forEach {
                requestData.args[it.first] = it.second
            }


            output = PrintStream(socket.getOutputStream())
            if (requestData.urls.size == 1 && requestData.urls[0].isEmpty()) {
                requestData.urls = arrayListOf("index")
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