package com.pine.lib.app

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent

open class PineActivity : Activity() {
    override fun onResume() {
        StaticPineActivity.onResume(this)
        super.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        StaticPineActivity.onKeyDown(this, keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        StaticPineActivity.onCreate(this)
        super.onCreate(savedInstanceState)
    }
}