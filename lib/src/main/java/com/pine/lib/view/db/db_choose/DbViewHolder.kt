package com.pine.lib.view.db.db_choose

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pine.lib.R

class DbViewHolder : ViewHolder {
    var dbName: TextView? = null

    constructor(v: View) : super(v) {
        dbName = v.findViewById(R.id.dbName)
    }


}
