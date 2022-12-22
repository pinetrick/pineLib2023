package com.pine.lib.addone.app_broadcast

import com.pine.lib.debug.d
import com.pine.lib.debug.i
import com.pine.lib.debug.w
import java.lang.ref.WeakReference

object AppBroadcastController {

    private var broadcasts = ArrayList<BroadcastRegBean>()

    private fun cleanNull() {
        var y: Int? = 0
        for (i in broadcasts.indices) {
            val broadcast = broadcasts[i - y!!]
            if (broadcast.onBroadcast.get() == null) {
                broadcasts.removeAt(i - y)
                y++
            }

        }
    }
    fun reg(key: String, callback: (String, Any?) -> Unit): Boolean {
        return reg(key, object : OnBroadcast {
            override fun onBroadcast(key: String, withObject: Any?) {
                callback(key, withObject)
            }
        })
    }

    fun reg(key: String, callback: OnBroadcast): Boolean {
        cleanNull()
        for (broadcast in broadcasts) {
            if (broadcast.key == key) {
                if (broadcast.onBroadcast.get() != null) {
                    if (broadcast.onBroadcast.get() == callback) {
                        w("Cannot reg Broadcast(Already Exist): $key")
                        return false
                    }
                }
            }
        }

        broadcasts.add(BroadcastRegBean(key, WeakReference(callback)))
        i("Reg Broadcast(Total: ${broadcasts.size}): $key")
        return true
    }

    fun send(key: String, withObject: Any? = null) {
        i("Sending Broadcast: $key")
        cleanNull()

        for (broadcast in broadcasts) {
            if (broadcast.key == key) {
                if (broadcast.onBroadcast.get() != null) {
                    d("$key -> ${broadcast.onBroadcast.get().toString()}")
                    broadcast.onBroadcast.get()!!.onBroadcast(key, withObject)
                }
            }
        }

    }

}