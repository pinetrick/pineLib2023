package com.pine.lib.app

import com.pine.lib.debug.libDb

class OnAppCrashCallback {
    constructor() {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            libDb?.let { it.recordCrash(thread, throwable) }
        }
    }
}