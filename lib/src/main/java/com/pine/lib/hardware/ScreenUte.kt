package com.pine.lib.hardware

import android.view.WindowManager
import com.pine.lib.app.a


fun keepScreenOn(needOn: Boolean){
    if (needOn){
        a().window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    else{
        a().window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
