package com.pine.lib_proj

import android.app.Application
import com.pine.lib.app.PineApplication

class MyApplication: Application() {

    override fun onCreate() {
        PineApplication.onCreate(this)
        super.onCreate()
    }
}