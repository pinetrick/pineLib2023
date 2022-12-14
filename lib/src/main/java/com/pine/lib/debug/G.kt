package com.pine.lib.debug

import android.util.Log
import com.pine.lib.app.C
import com.pine.lib.view.toast.toast

var tag = "--- PineDebug ---"

fun d(s: Any) {
    if (C.isDebug) {
        Log.d(tag, s.toString());
    }


}

fun i(s: Any) {
    if (C.isDebug) {
        Log.i(tag, s.toString());
    }
}

fun w(s: Any) {
    Log.w(tag, s.toString());
    if (C.isDebug) {
        toast(s)
    }
}

fun e(s: Any) {
    Log.e(tag, s.toString());
    if (C.isDebug) {
        toast(s)
    }
}


