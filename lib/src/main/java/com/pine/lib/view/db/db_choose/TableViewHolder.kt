package com.pine.lib.view.db.db_choose

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pine.lib.R

class TableViewHolder : ViewHolder {
    var baseView: View
    var tableName: TextView? = null

    constructor(v: View) : super(v) {
        baseView = v
        tableName = v.findViewById(R.id.tableName)
    }


}
