package com.pine.lib.view.db.db_choose

import android.os.Bundle
import android.widget.*
import com.pine.lib.R
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import com.pine.lib.app.PineAppCompatActivity
import com.pine.lib.app.a


class DbChooseActivity : PineAppCompatActivity() {

    lateinit var table: TableLayout

    lateinit var sqlEditor: EditText

    lateinit var closeButton: ImageView
    lateinit var refreshButton: ImageView

    lateinit var dbList: Spinner
    lateinit var tableList: Spinner

    lateinit var dbAdapter: DbAdapter
    lateinit var tableAdapter: TableAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_choose_activity)

        closeButton = findViewById(R.id.database_close_button)
        refreshButton = findViewById(R.id.database_refresh)
        dbList = findViewById(R.id.database_db_list)
        tableList = findViewById(R.id.database_table_list)
        table = findViewById(R.id.database_table)
        sqlEditor = findViewById(R.id.database_sql_editor)

        dbAdapter = DbAdapter(this, android.R.layout.simple_spinner_item)
        dbList.adapter = dbAdapter
        dbAdapter.onDbChoosed = ::onDbChoose
        dbList.onItemSelectedListener = dbAdapter

        tableAdapter = TableAdapter(this, android.R.layout.simple_spinner_item)
        tableList.adapter = tableAdapter
        tableAdapter.onTableChoosed = ::onTableChoose
        tableList.onItemSelectedListener = tableAdapter


        closeButton.setOnClickListener { this.finish() }
        refreshButton.setOnClickListener { this.refreshDatabase() }
        addFakeData()
        refreshDatabase()
    }


    private fun refreshDatabase() {
        val databases = Db.getAllDb()
        dbAdapter.setDatabase(databases)

        //选中第一个数据库
        val selectId = databases.firstOrNull {
            it !in arrayOf("")
        }
        selectId?.let {
            dbList.setSelection(databases.indexOf(it))
        }


    }

    private fun onDbChoose(dbName: String?) {
        if (dbName == null) return

        val tables = Db(dbName).tables()
        tableAdapter.setTables(dbName, tables)

        //选中第一个table
        val selectId = tables.firstOrNull {
            it !in arrayOf("room_master_table", "sqlite_sequence")
        }
        selectId?.let {
            tableList.setSelection(tables.indexOf(it))
        }
    }

    private fun onTableChoose(dbName: String, tableName: String?) {
        if (tableName == null) return

        refreshTable(dbName, tableName)
    }

    private fun refreshTable(dbName: String, tableName: String) {
        table.removeAllViewsInLayout()
        val records = Db(dbName).model(tableName).select()

        var row = TableRow(a())
        records.headers.forEach {
            val textView = TableHeaderTextView(a())
            textView.init(it.name + "\r\n[ " + it.type + " ]")
            row.addView(textView)

        }
        table.addView(row)

        records.records.forEach { record ->
            row = TableRow(a())
            records.headers.forEach { header ->
                val editText = TableEditText(a())
                editText.init(record, header.name)
                row.addView(editText)
            }
            table.addView(row)
        }


    }

    fun addFakeData() {
        val db = Db("TestDb2")
        val table = db.model("Users1")
        table.create {
            it.apply {
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("colume1", "TEXT"))
                add(TableHeader("colume2", "TEXT"))
                add(TableHeader("colume3", "TEXT"))
                add(TableHeader("colume4", "TEXT"))
                add(TableHeader("colume5", "TEXT"))
                add(TableHeader("colume6", "TEXT"))
                add(TableHeader("colume7", "TEXT"))
                add(TableHeader("colume8", "TEXT"))
                add(TableHeader("colume9", "TEXT"))
            }
        }

        var record = table.newRecord()
            .put("colume1", "Unknown Value")
            .put("colume2", "Unknown Value")
            .put("colume3", "Unknown Value")
            .put("colume4", null)
            .put("colume5", "Unknown Value")
            .put("colume6", "Unknown Value")
            .put("colume7", "Unknown Value")
            .put("colume8", "Unknown Value")
            .put("colume9", "Unknown Value")
            .save()

        record = table.select().records[0]
        record.put("colume1", "xx")
        record.save()

        sqlEditor.setText(db.lastSql)
    }
}