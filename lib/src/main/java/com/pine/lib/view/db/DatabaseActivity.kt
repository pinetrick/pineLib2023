package com.pine.lib.view.db

import android.app.Activity
import android.os.Bundle
import com.pine.lib.R
import com.pine.lib.addone.db.Db
import com.pine.lib.app.PineActivity

class DatabaseActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        PineActivity.onCreate(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_activity)

        refresh()
    }


    override fun onResume() {
        PineActivity.onResume(this)
        super.onResume()
    }

    fun refresh(){

        Db("TestDb2")
        val table = Db("TestDb1").model("Users")
        table.create {
            it.apply {
                add(Pair("id", "INTEGER PRIMARY KEY"))
                add(Pair("xx", "TEXT"))
            }
        }

        val db = Db.getAllDb()
        val header = table.headers

    }
}