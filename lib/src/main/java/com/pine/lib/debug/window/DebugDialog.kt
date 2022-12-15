package com.pine.lib.debug.window


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.pine.lib.R
import com.pine.lib.app.a


class DebugDialog : AlertDialog {
    private var mainMassage: TextView? = null

    constructor(context: Context?, theme: Int) : super(context, theme) {}
    constructor(context: Context?) : super(context) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debug_window_window)
        mainMassage = findViewById<TextView>(R.id.mainMassage)
        var activityName = ""
        if (a() != null) {
            activityName = a().localClassName
        }
        mainMassage!!.text = "$activityName"
    }

    val view: DebugDialog
        get() = this
}