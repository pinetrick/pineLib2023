package com.pine.lib.view.db.db_choose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R


class DataAdapter : RecyclerView.Adapter<TableViewHolder>() {

    var onTableChoosed: ((dbName: String, tableName: String) -> Unit)? = null
    var dbName: String = ""
    var tables: List<String> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.database_choose_table_adapter, parent, false)
        return TableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tables.size
    }


    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.tableName!!.text = tables[position]
        holder.baseView!!.setOnClickListener {
            onTableChoosed?.let {
                onTableChoosed!!(dbName, tables[position])
            }
        }
    }

    fun setTable(dbName: String, tables: List<String>) {
        this.dbName = dbName
        this.tables = tables
        notifyDataSetChanged()

    }
}