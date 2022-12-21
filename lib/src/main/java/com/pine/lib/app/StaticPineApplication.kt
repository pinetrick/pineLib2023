package com.pine.lib.app

import android.app.Application
import com.pine.lib.debug.LibDb


object StaticPineApplication {
    var application: Application? = null
    var onAppCrashCallback = OnAppCrashCallback()

    fun onCreate(application: Application) {
        this.application = application


        if (C.isDebug) {
            onDebugInit()
        } else {
            onLiveInit()
        }

        LibDb()
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