package com.pine.lib_proj

import android.os.StrictMode
import com.pine.lib.app.PineApplication

class MyApplication : PineApplication() {

    override fun onCreate() {
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build()
        )
        super.onCreate()
    }

}