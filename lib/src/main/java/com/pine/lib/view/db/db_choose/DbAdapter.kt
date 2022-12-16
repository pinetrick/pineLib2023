package com.pine.lib.view.db.db_choose


import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter


class DbAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource),
    AdapterView.OnItemSelectedListener {

    var onDbChoosed: ((choosedName: String?) -> Unit)? = null
    var databases: List<String> = emptyList()


    fun setDatabase(databases: List<String>) {
        this.databases = databases
        clear()
        addAll(databases)
        notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        onDbChoosed?.let {
            onDbChoosed!!(databases[position])
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        onDbChoosed?.let {
            onDbChoosed!!(null)
        }
    }
}