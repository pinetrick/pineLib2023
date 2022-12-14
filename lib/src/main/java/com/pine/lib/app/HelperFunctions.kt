package com.pine.lib.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pine.lib.debug.e

private var mGson: Gson? = null
fun gson(): Gson {
    if (mGson == null) {
        mGson = GsonBuilder().serializeNulls().create()
    }
    return mGson!!
}

fun a(): Activity {
    try {
        StaticPineActivity.activity.get()?.let {
            return it
        }
    } catch (e: Exception) {
        e("activity没有被注入")
    }

    throw IllegalArgumentException("activity没有被注入")
}

fun isActivityInjected(): Boolean {
    try {
        StaticPineActivity.activity.get()?.let {
            return true
        }
    } catch (_: Exception) {

    }
    return false
}

fun app(): Application {
    return StaticPineApplication.application!!
}

fun c(): Context {
    return app().applicationContext
}

fun stringResources(id: Int): String {
    return c().getString(id)
}

fun drawable(id: Int): Drawable {
    return AppCompatResources.getDrawable(c(), id)!!
}

@Suppress("DEPRECATION")
fun color(id: Int): Int {
    val version = Build.VERSION.SDK_INT
    return if (version >= 23) {
        ContextCompat.getColor(c(), id)
    } else {
        c().resources.getColor(id)
    }
}



