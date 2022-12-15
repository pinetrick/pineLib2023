package com.pine.lib.debug.window

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pine.lib.app.a


class DebugWindowAdapter : BaseAdapter() {
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return DbgButton.i().availableBtns.size
    }

    override fun getItem(arg0: Int): Any? {
        // TODO Auto-generated method stub
        return null
    }

    override fun getItemId(arg0: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(arg0: Int, arg1: View?, arg2: ViewGroup?): View {
        val textView = TextView(a())
        textView.setText(DbgButton.i().availableBtns[arg0].getBtnText())
        textView.setBackgroundColor(Color.rgb(255, 255, 0))
        textView.minimumHeight = 10
        textView.setTextColor(Color.rgb(0, 0, 0))
        return textView
    }
}