package com.pine.lib.view.db.db_choose

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.pine.lib.R

class TableEditText : AppCompatEditText {


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        textSize = 10.0f
        height = 30
        setPadding(0, 0, 0, 0)
        setBackgroundResource(R.drawable.database_background_with_border)
        setTextColor(Color.BLACK)

    }
}