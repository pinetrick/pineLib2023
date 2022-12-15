package com.pine.lib.debug.window

import android.content.Context

interface IDebugBtns {
    fun getBtnText(): String

    fun onBtnClick(context: Context)

}