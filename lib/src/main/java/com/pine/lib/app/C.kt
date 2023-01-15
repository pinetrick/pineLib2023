package com.pine.lib.app

object C {

    var isDebug: Boolean = true;
    var keepScreenOn: Boolean = true;
    var releaseSignature: String = "";

    val isAppInDebugModel: Boolean by lazy {
        try {
            val packageName = c().packageName
            val buildConfig = Class.forName("$packageName.BuildConfig")
            val DEBUG = buildConfig.getField("DEBUG")
            DEBUG.isAccessible = true
            DEBUG.getBoolean(null)
        }
        catch (e: Exception) {
            false
        }
    }
}