package com.pine.lib.provider.server_socket

import android.content.Context
import android.content.res.AssetManager
import com.pine.lib.addone.base_class_ext.indexOfArray
import com.pine.lib.addone.base_class_ext.writeln
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

    fun handle(socket: Socket) {
        var input: DataInputStream? = null
        var output: OutputStream? = null
        val requestData = RequestData()
        val responseData = ResponseData()

        try {
            // Read HTTP headers and parse out the route.
            input = DataInputStream(socket.getInputStream())
            output = socket.getOutputStream()

            var dataTotalLength = 0

            val data = ByteArray(102400)
            val outSteam = ByteArrayOutputStream()
            var readLength = 0
            do {
                val readLength = input.read(data)
                dataTotalLength += readLength
                outSteam.write(data, 0, readLength)
                if (input.available() <= 0) break;
            } while (true)

            var allByteData = outSteam.toByteArray()
            val controlDataLength = allByteData.indexOfArray("\r\n\r\n".toByteArray())
            val controlData = allByteData.sliceArray(0 until controlDataLength)
            val controlDataString = String(controlData, Charsets.UTF_8)

            val requestMethod = Regex("^(GET|POST|PUT)").find(controlDataString)?.value
            val path = Regex("^\\w+\\s(/[^\\s^\\?^#]*)").find(controlDataString)?.groupValues?.get(1)
            val params = Regex("([^&=?]+)=([^&=\\s]+)").findAll(controlDataString.split("\r\n")[0])
                .map { it.groupValues[1] to it.groupValues[2] }.toList()
            val contentLength =
                Regex("Content-Length: (\\d+)").find(controlDataString)?.groupValues?.get(1)?.toInt() ?: 0

            if (contentLength > 0) {
                // 解析 POST 数据
                while (dataTotalLength < contentLength + controlDataLength) {
                    val readLength = input.read(data)
                    dataTotalLength += readLength
                    outSteam.write(data, 0, readLength)
                }
                allByteData = outSteam.toByteArray()
                val postData = allByteData.sliceArray(controlDataLength + 4 until  allByteData.size)
                val postDataString = String(postData)

                Regex("([^&=?]+)=([^&=]+)").findAll(postDataString).forEach {
                    requestData.bodyArgs[it.groupValues[1]] =
                        URLDecoder.decode(it.groupValues[2], Charsets.UTF_8.name())
                }
            }

            requestData.method = requestMethod!!
            requestData.urls = path!!.removePrefix("/").split("/")
            params.forEach {
                requestData.args[it.first] = it.second
            }


            if (requestData.urls.size == 1 && requestData.urls[0].isEmpty()) {
                requestData.urls = arrayListOf("index.html")
            }



            responseData.request = requestData
            responseHandler.getResponse(requestData, responseData)

            responseData.writeResponse(output)



           // d(controlDataString)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                output?.close()
                input?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}