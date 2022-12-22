package com.pine.lib.provider.server_socket

import android.text.TextUtils
import com.pine.lib.app.gson
import java.io.PrintStream

data class ResponseData(
    var request: RequestData = RequestData(),
    var content: String? = null,
    var contentByte: ByteArray? = null,
    var returnObj: Any? = null,
) {
    fun writeResponse(output: PrintStream) {
        val responseByteArray = content?.toByteArray() ?: contentByte ?: gson().toJson(returnObj).toByteArray()

        // Send out the content.
        output.println("HTTP/1.0 200 OK")
        output.println("Content-Type: " + getContentType())
        output.println("Content-Length: " + responseByteArray!!.size)
        output.println()
        output.write(responseByteArray)
        output.flush()
    }


    private fun getContentType(): String? {
        var type: String = request.urls.last()

        return if (TextUtils.isEmpty(type)) {
            null
        } else if (type.endsWith(".html")) {
            "text/html"
        } else if (type.endsWith(".js")) {
            "application/javascript"
        } else if (type.endsWith(".css")) {
            "text/css"
        } else if (type.endsWith(".zip")) {
            "application/octet-stream"
        } else {
            "text/html"
        }
    }
}