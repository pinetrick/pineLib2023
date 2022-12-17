package com.pine.lib.app

import android.app.Application

open class PineApplication : Application() {

    override fun onCreate() {
        StaticPineApplication.onCreate(this)
        super.onCreate()
    }
}