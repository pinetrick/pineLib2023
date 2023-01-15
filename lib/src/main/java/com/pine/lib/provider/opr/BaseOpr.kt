package com.pine.lib.provider.opr

import com.pine.lib.provider.server_socket.RequestData
import com.pine.lib.provider.server_socket.ResponseData

open class BaseOpr {
    lateinit var requestData: RequestData
    lateinit var responseData: ResponseData

    fun run(requestData: RequestData, responseData: ResponseData) {
        try {
            this.requestData = requestData
            this.responseData = responseData

            val func = this::class.java.getDeclaredMethod(requestData.urls[1])

            func.invoke(this)
        } catch (e: Exception) {
            responseData.content = e.stackTraceToString().replace("\r\n", "<br>")
        }
    }

    fun success(info: String = "Success") {
        responseData.returnObj = info
    }

    fun error(info: String = "Error") {
        responseData.returnObj = info
    }
}