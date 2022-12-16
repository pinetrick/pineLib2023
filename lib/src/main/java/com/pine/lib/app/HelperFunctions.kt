package com.pine.lib.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import com.pine.lib.debug.e

fun a(): Activity {
    PineActivity.activity.get()?.let {
        return it
    }
    e("activity没有被注入")
    throw IllegalArgumentException("activity没有被注入")
}

fun app(): Application {
    return PineApplication.application
}

fun c(): Context {
    return app().applicationContext
}

fun stringResources(id: Int): String{
    return c().getString(id)
}

fun drawable(id: Int): Drawable {
    return c().getDrawable(id)!!
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



