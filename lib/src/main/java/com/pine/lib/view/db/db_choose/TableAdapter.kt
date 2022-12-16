package com.pine.lib.view.db.db_choose

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter


class TableAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource),
    AdapterView.OnItemSelectedListener {

    var onTableChoosed: ((dbName: String, tableName: String?) -> Unit)? = null
    var dbName: String = ""
    var tables: List<String> = emptyList()


    fun setTables(dbName:String, tables: List<String>) {
        this.dbName = dbName
        this.tables = tables
        clear()
        addAll(tables)
        notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        onTableChoosed?.let {
            onTableChoosed!!(dbName, tables[position])
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        onTableChoosed?.let {
            onTableChoosed!!(dbName, null)
        }
    }
}