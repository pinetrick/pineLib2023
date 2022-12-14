package com.pine.lib.app

import android.app.Application


object PineApplication {
    lateinit var application: Application

    fun onCreate(application: Application) {
        this.application = application


        if (C.isDebug) {
            onDebugInit()
        } else {
            onLiveInit()
        }
    }

    fun onDebugInit() {
        C.isDebug = true
        C.keepScreenOn = true
    }

    fun onLiveInit() {
        C.isDebug = false
        C.keepScreenOn = false
    }

}