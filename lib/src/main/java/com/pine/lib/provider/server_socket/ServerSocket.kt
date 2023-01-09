package com.pine.lib.provider.server_socket

import com.pine.lib.app.c
import com.pine.lib.debug.e
import com.pine.lib.debug.i
import com.pine.lib.net.NetworkUtils
import java.io.IOException
import java.net.ServerSocket
import java.net.SocketException


class ClientServer(port: Int = 8080) : Runnable {
    private val mPort: Int
    private val mRequestHandler: RequestHandler
    var isRunning = false
        private set
    private var mServerSocket: ServerSocket? = null


    init {
        mRequestHandler = RequestHandler(c())
        mPort = port
    }

    fun start() {
        isRunning = true
        Thread(this).start()
        i(NetworkUtils.getAddressLog(mPort))
    }

    fun stop() {
        try {
            isRunning = false
            if (null != mServerSocket) {
                mServerSocket!!.close()
                mServerSocket = null
            }
        } catch (e: Exception) {
            e("Error closing the server socket.")
        }
    }

    override fun run() {
        try {
            mServerSocket = ServerSocket(mPort)
            while (isRunning) {
                try {
                    val socket = mServerSocket!!.accept()
                    mRequestHandler.handle(socket)
                    socket.close()
                } catch (e: SocketException) {
                    // The server was stopped; ignore.
                    e("SocketException")
                    e.printStackTrace()
                } catch (e: IOException) {
                    e("Web server error.")
                } catch (ignore: Exception) {
                    e("Exception.")
                    ignore.printStackTrace()
                }
            }
        }
        catch (e: Exception) {
            e("Server cannot create, cannot open port, may in used?")
        }

    }
}