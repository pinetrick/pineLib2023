package com.pine.lib.provider.server_socket

import android.content.Context
import android.content.res.AssetManager
import android.text.TextUtils
import java.io.*
import java.net.Socket
import java.util.*


/**
 * Created by amitshekhar on 06/02/17.
 */
class RequestHandler(private val mContext: Context) {
    private val mAssets: AssetManager
    private var mSelectedDatabase: String? = null

    private var responseData = HttpResponseData()

    init {
        mAssets = mContext.resources.assets
    }

    @Throws(IOException::class)
    fun handle(socket: Socket) {
        var reader: BufferedReader? = null
        var output: PrintStream? = null
        try {
            var route: String? = null

            // Read HTTP headers and parse out the route.
            reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            var line: String
            while (!TextUtils.isEmpty(reader.readLine().also { line = it })) {
                if (line.startsWith("GET /")) {
                    val start = line.indexOf('/') + 1
                    val end = line.indexOf(' ', start)
                    route = line.substring(start, end)
                    break
                }
            }

            // Output stream that we send the response to
            output = PrintStream(socket.getOutputStream())
            if (route == null || route.isEmpty()) {
                route = "index.html"
            }
            val bytes = responseData.getResponse(route)

            if (null == bytes) {
                writeServerError(output)
                return
            }

            // Send out the content.
            output.println("HTTP/1.0 200 OK")
            output.println("Content-Type: " + detectMimeType(route))
            if (route.startsWith("downloadDb")) {
                output.println("Content-Disposition: attachment; filename=$mSelectedDatabase")
            } else {
                output.println("Content-Length: " + bytes.size)
            }
            output.println()
            output.write(bytes)
            output.flush()
        } finally {
            try {
                output?.close()
                reader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun detectMimeType(fileName: String): String? {
        return if (TextUtils.isEmpty(fileName)) {
            null
        } else if (fileName.endsWith(".html")) {
            "text/html"
        } else if (fileName.endsWith(".js")) {
            "application/javascript"
        } else if (fileName.endsWith(".css")) {
            "text/css"
        } else {
            "application/octet-stream"
        }
    }

    private fun writeServerError(output: PrintStream) {
        output.println("HTTP/1.0 500 Internal Server Error")
        output.flush()
    }


}