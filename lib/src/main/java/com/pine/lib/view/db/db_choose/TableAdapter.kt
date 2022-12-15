package com.pine.lib.view.db.db_choose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R


class TableAdapter : RecyclerView.Adapter<TableViewHolder>() {

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

    }

    fun setTable(tables: List<String>) {
        this.tables = tables
        notifyDataSetChanged()

    }
}