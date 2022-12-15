package com.pine.lib.view.message_box

import com.pine.lib.R

class MessageBoxObj {
    var title = ""
    var hint = ""

    var callback: ((clickedBtnIdFrom1: Int) -> Unit)? = null

    var baseLayout: Int = R.layout.message_box_black_normal
    var dialog: MessageBoxView? = null

    var cancelable: Boolean = false

    lateinit var btns: Array<out String>


}
