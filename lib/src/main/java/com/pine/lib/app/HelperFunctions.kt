package com.pine.lib.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat

fun app(): PineApplication{
    return PineApplication.i();
}

fun c(): Context {
    return app().applicationContext;
}

fun stringResources(id: Int): String{
    return c().getString(id);
}

fun drawable(id: Int): Drawable {
    return c().getDrawable(id)!!;
}

@Suppress("DEPRECATION")
fun color(id: Int): Int {
    var version = Build.VERSION.SDK_INT;
    return if (version >= 23) {
        ContextCompat.getColor(c(), id)
    } else {
        c().resources.getColor(id);
    }
}



