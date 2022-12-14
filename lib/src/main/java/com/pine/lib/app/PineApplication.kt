package com.pine.lib.app

import android.support.multidex.MultiDexApplication
import com.pine.lib.debug.e


open class PineApplication : MultiDexApplication {

    constructor() {
        baseApp = this
    }

    override fun onCreate() {
        super.onCreate()

        if (C.isDebug) {
            onDebugInit();
        } else {
            onLiveInit();
        }
    }

    open fun onDebugInit() {
        C.isDebug = true;
        C.keepScreenOn = true;
    }

    open fun onLiveInit() {
        C.isDebug = false;
        C.keepScreenOn = false;
    }


    companion object {
        var baseApp: PineApplication? = null;

        fun i(): PineApplication {
            if (baseApp == null) {
                e("严重错误，Application 没有被初始化！");
            }
            return baseApp!!
        }

    }

}