package com.pine.lib.provider.opr

import com.pine.lib.debug.e
import com.pine.lib.debug.libDb

class Crash : BaseOpr() {


    fun listCrash() {
        val crash = libDb?.db?.model("crash")

        crash?.let {
            val crashes = it.order("id desc").limit(30).select()
            responseData.returnObj = crashes
        }
    }

    fun show() {
        val crash = libDb?.db?.model("crash")?.where("id", requestData.urls[2])?.select()

        crash?.let {
            it.records?.firstOrNull()?.values?.get("details")?.let {
                e(it)
            }
        }

    }
}