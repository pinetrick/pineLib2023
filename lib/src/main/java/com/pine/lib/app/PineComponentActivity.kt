package com.pine.lib.app

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity

open class PineComponentActivity: ComponentActivity() {
    override fun onResume() {
        PineActivity.onResume(this)
        super.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        PineActivity.onKeyDown(this, keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        PineActivity.onCreate(this)
        super.onCreate(savedInstanceState)
    }
}