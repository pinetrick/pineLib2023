package com.pine.lib.view.db.db_choose

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.pine.lib.R


class TableHeaderTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    fun init(key: String) {
        text = key

        textSize = 10.0f
        height = 30
        textAlignment = TEXT_ALIGNMENT_CENTER
        setTypeface(null, Typeface.BOLD)

        setPadding(4,4,4,4)
        setBackgroundResource(R.drawable.database_background_with_border)
        setTextColor(Color.BLACK)

    }

}