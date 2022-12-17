package com.pine.lib.debug.window


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.pine.lib.R
import com.pine.lib.app.a
import com.pine.lib.app.isActivityInjected


class DebugDialog(context: Context?, theme: Int) : AlertDialog(context, theme) {
    private var mainMassage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debug_window_window)
        mainMassage = findViewById<TextView>(R.id.mainMassage)
        var activityName = ""
        if (isActivityInjected()) {
            activityName = a().javaClass.simpleName
        }
        mainMassage!!.text = activityName
    }

    val view: DebugDialog
        get() = this
}