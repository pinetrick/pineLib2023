package com.pine.lib.view.db.db_choose

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.pine.lib.R
import com.pine.lib.addone.db.Record

class TableEditText : AppCompatEditText {

    var record: Record? = null
    var key: String = ""
    var value: String = ""

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    fun init(record: Record, key: String){
        this.record = record
        this.key = key
        this.value = record.values[key].toString()

        setText(value)

        textSize = 10.0f
        height = 30
        setPadding(0, 0, 0, 0)
        setBackgroundResource(R.drawable.database_background_with_border)
        setTextColor(Color.BLACK)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if ((!focused) && (this.text.toString() != value)) {
            record?.let {
                record!!.values[key] = this.text.toString()
                it.save()
            }
        }
    }

}