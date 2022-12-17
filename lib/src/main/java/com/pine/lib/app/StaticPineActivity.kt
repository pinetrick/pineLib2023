package com.pine.lib.app

import android.app.Activity
import android.view.KeyEvent
import com.pine.lib.addone.MyTimer
import com.pine.lib.app.C.keepScreenOn
import com.pine.lib.debug.window.DbgButton
import com.pine.lib.hardware.keepScreenOn
import com.pine.lib.view.toast.toast
import java.lang.ref.WeakReference

object StaticPineActivity {

    //是否开启双击退出
    var enableDoubleReturnExit = false
    lateinit var activity: WeakReference<Activity>

    fun onCreate(activity: Activity) {
        StaticPineActivity.activity = WeakReference(activity)
        DbgButton.i()

        keepScreenOn(keepScreenOn)

    }

    fun onResume(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    private var mBackKeyPressed = false

    //调用这个函数可以实现返回两次退出
    fun onKeyDown(activity: Activity, keyCode: Int, event: KeyEvent?) {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (enableDoubleReturnExit) {
                    if (!mBackKeyPressed) {
                        toast("Press one more times for exit")
                        mBackKeyPressed = true
                        MyTimer().setInterval(2000).setOnTimerListener {
                            mBackKeyPressed = false
                        }.start(1)
                        return
                    } else {//退出程序
                        activity.finish()
                        //System.exit(0)
                    }
                }
            }
            else -> {
            }
        }
    }


}