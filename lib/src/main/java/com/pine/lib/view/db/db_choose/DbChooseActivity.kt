package com.pine.lib.view.db.db_choose

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import com.pine.lib.app.PineActivity

class DbChooseActivity : Activity() {

    lateinit var dbsView: RecyclerView
    lateinit var tablesView: RecyclerView

    var dbAdapter = DbAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        PineActivity.onCreate(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_choose_activity)

        dbsView = findViewById(R.id.dbs)
        tablesView = findViewById(R.id.tables)

        dbsView.adapter = dbAdapter


        addFakeData()
        refresh()
    }


    override fun onResume() {
        PineActivity.onResume(this)
        super.onResume()
    }

    fun refresh() {
//        tableLayout.removeAllViewsInLayout()
//
//        val records = Db("TestDb1").model("Users").select()
//
//        records.forEach {
//            val tableRow = TableRow(this)
//
//            val editText = EditText(this)
//            editText.setText(it.values.values.first().toString())
//
//            tableRow.addView(editText)
//
//
//            tableLayout.addView(tableRow)
//
//        }


    }

    fun addFakeData() {

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