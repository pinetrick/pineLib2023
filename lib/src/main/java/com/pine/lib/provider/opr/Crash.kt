package com.pine.lib.provider.opr

import com.pine.lib.app.gson
import com.pine.lib.debug.e
import com.pine.lib.debug.libDb

class Crash {


    fun run(route: List<String>): String {
        try {
            val func = this::class.java.getDeclaredMethod(route[1], List::class.java)

            return func.invoke(this, route) as String
        } catch (e: Exception) {
            return e.stackTraceToString().replace("\r\n", "<br>")
        }
    }

    fun listCrash(route: List<String>): String {
        val crash = libDb?.db?.model("crash")

        crash?.let {
            val crashes = it.order("id desc").limit(30).select()
            return gson().toJson(crashes)
        }

        return gson().toJson(null)
    }

    fun show(route: List<String>): String {
        val crash = libDb?.db?.model("crash")?.where("id", route[2])?.select()

        crash?.let {
            it.records?.firstOrNull()?.values?.get("details")?.let {
                e(it)
            }
        }

        return gson().toJson(null)
    }
}