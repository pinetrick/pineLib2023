package com.pine.lib.view.db.db_choose


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R
import com.pine.lib.app.c


class DbAdapter : RecyclerView.Adapter<DbViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DbViewHolder {
        val view: View =
            LayoutInflater.from(c()).inflate(R.layout.database_choose_db_adapter, parent, false)
        return DbViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 20
    }


    override fun onBindViewHolder(holder: DbViewHolder, position: Int) {
        holder.dbName!!.text = "xxx"

    }
}