package com.pine.lib.addone.db

import android.database.Cursor

class Records {

    var db: Db? = null
    var table: Table? = null
    var dbName = ""
    var tableName: String? = null
    var headers: ArrayList<TableHeader> = ArrayList()
    var records: ArrayList<Record> = ArrayList()

    fun initHeadersBaseARecord(db: Db, table: Table, c: Cursor) {
        this.db = db
        this.table = table
        this.dbName = db.dbName
        this.tableName = table.tableName
        this.headers = ArrayList()

        (0 until c.columnCount).forEach{
            val th = TableHeader()
            th.name = c.getColumnName(it)
            th.type = when (c.getType(it)){
                Cursor.FIELD_TYPE_BLOB -> "blob"
                Cursor.FIELD_TYPE_FLOAT -> "float"
                Cursor.FIELD_TYPE_INTEGER -> "integer"
                Cursor.FIELD_TYPE_NULL -> "null"
                Cursor.FIELD_TYPE_STRING -> "text"
                else -> "unknown"
            }

            headers.add(th)
        }

    }

    fun anylizeLine(c: Cursor) {
        val record = Record(db!!, table!!)
        record.isNewRecord = false

        headers.forEachIndexed { index, it ->
            val v = when (it.type.lowercase()) {
                "text" -> c.getString(index)
                "integer" -> c.getInt(index)
                else -> "Unknown"
            }
            record.values[it.name] = v
        }

        records.add(record)
    }

}