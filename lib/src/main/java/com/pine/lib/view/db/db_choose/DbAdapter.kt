package com.pine.lib.view.db.db_choose


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R


class DbAdapter : RecyclerView.Adapter<DbViewHolder>() {

    var onDbChoosed: ((choosedName: String) -> Unit)? = null
    var databases: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DbViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.database_choose_db_adapter, parent, false)
        return DbViewHolder(view)
    }

    override fun getItemCount(): Int {
        return databases.size
    }


    override fun onBindViewHolder(holder: DbViewHolder, position: Int) {
        holder.dbName!!.text = databases[position]
        holder.baseView!!.setOnClickListener {
            onDbChoosed?.let {
                onDbChoosed!!(databases[position])
            }
        }
    }

    fun setDatabase(databases: List<String>) {
        this.databases = databases
        notifyDataSetChanged()

    }
}