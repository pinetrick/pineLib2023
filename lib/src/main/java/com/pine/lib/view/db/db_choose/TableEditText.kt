package com.pine.lib.view.db.db_choose

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.pine.lib.R
import com.pine.lib.addone.db.Record

open class TableEditText : AppCompatEditText {

    var record: Record? = null
    var key: String = ""
    var value: Any? = null

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    open fun init(record: Record, key: String){
        this.record = record
        this.key = key
        this.value = record.values[key]

        setTextColor(Color.BLACK)
        when (this.value) {
            null -> {
                setText("- NULL -")
                setTextColor(Color.GRAY)
            }
            else -> setText(value.toString())

        }


        textSize = 10.0f
        height = 30
        setPadding(0, 0, 0, 0)
        setBackgroundResource(R.drawable.database_background_with_border)

    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if ((!focused) && (this.text.toString() != value)) {
            record?.let {
                record!!.values[key] = when (this.text.toString()) {
                    "- NULL -" -> null
                    else -> this.text.toString()
                }
                it.save()
            }
        }
    }

}