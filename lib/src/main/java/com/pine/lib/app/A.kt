package com.pine.lib.app

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pine.lib.addone.MyTimer
import com.pine.lib.app.C.keepScreenOn
import com.pine.lib.hardware.keepScreenOn
import com.pine.lib.view.toast.toast


fun a(): PineActivity {
    return PineActivity.activity!!
}

open class PineActivity : AppCompatActivity() {
    //是否开启双击退出
    open var enableDoubleReturnExit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        PineActivity.activity = this;
        super.onCreate(savedInstanceState)

        keepScreenOn(keepScreenOn);

    }

    override fun onResume() {
        PineActivity.activity = this;
        super.onResume()
    }

    //返回键按下事件，可被重写
    open fun onReturnKeyDown(): Boolean {
        return false;
    }

    private var mBackKeyPressed = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {

                if (enableDoubleReturnExit) {
                    if (!mBackKeyPressed) {
                        toast("Press one more times for exit")
                        mBackKeyPressed = true
                        MyTimer().setInterval(2000).setOnTimerListener {
                            mBackKeyPressed = false
                        }.start(1)
                        return false;
                    } else {//退出程序
                        this.finish()
                        //System.exit(0)
                    }
                }

                if (onReturnKeyDown())
                    return false;
            }
            else -> {
            }
        }


        return super.onKeyDown(keyCode, event)
    }



    companion object {
        var activity: PineActivity? = null;
    }

}