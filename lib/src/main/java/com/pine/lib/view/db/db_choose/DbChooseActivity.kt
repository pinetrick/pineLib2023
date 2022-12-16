package com.pine.lib.view.db.db_choose

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pine.lib.R
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import com.pine.lib.app.PineActivity
import com.pine.lib.app.PineAppCompatActivity
import com.pine.lib.app.intent
import com.pine.lib.view.db.show_table_data.ShowTableDataActivity

class DbChooseActivity : PineAppCompatActivity() {

    lateinit var dbsView: RecyclerView
    lateinit var tablesView: RecyclerView
    lateinit var closeButton: ImageView


    var dbAdapter = DbAdapter()
    var tableAdapter = TableAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_choose_activity)

        dbsView = findViewById(R.id.dbs)
        tablesView = findViewById(R.id.tables)
        closeButton = findViewById(R.id.database_close_button)


        dbsView.adapter = dbAdapter
        dbsView.layoutManager = LinearLayoutManager(this)

        tablesView.adapter = tableAdapter
        tablesView.layoutManager = LinearLayoutManager(this)

        closeButton.setOnClickListener { this.finish() }
        addFakeData()
        refreshDatabase()
    }


    private fun refreshDatabase() {

        val databases = Db.getAllDb()
        dbAdapter.setDatabase(databases)
        dbAdapter.onDbChoosed = ::onDbChoose

        tableAdapter.onTableChoosed = ::onTableChoose

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

    fun onDbChoose(dbName: String) {
        val tables = Db(dbName).tables()
        tableAdapter.setTable(dbName, tables)
    }

    fun onTableChoose(dbName: String, tableName: String) {

        ShowTableDataActivity.dbName = dbName
        ShowTableDataActivity.tableName = tableName

        intent(ShowTableDataActivity::class)
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