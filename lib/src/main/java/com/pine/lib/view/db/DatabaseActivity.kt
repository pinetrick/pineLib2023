package com.pine.lib.view.db

import android.app.Activity
import android.os.Bundle
import com.pine.lib.R
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
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
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("xx", "TEXT"))
            }
        }

        val table1 = Db("TestDb1").model("Users5")
        table1.create {
            it.apply {
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("xx", "TEXT"))
            }
        }

        table1.newRecord().put("xx", "Unknown Value").save()
        table1.newRecord().put("xx", "Unknown Value1").save()

        val db = Db.getAllDb()
       // val header = table.headers
        val records = table.select()
    }
}