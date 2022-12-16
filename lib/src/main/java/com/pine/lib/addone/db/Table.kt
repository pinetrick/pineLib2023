package com.pine.lib.addone.db

import android.database.Cursor
import androidx.core.database.getStringOrNull

class Table constructor(val db: Db, val tableName: String) {

    val headers: ArrayList<TableHeader> by lazy {
        if (!db.db.isOpen) {
            ArrayList()
        } else {
            val c: Cursor = db.db.rawQuery("pragma table_info ('$tableName');", null)

            val r: ArrayList<TableHeader> = ArrayList<TableHeader>()

            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    val tableHeader = TableHeader()
                    tableHeader.cid = c.getInt(0)
                    tableHeader.name = c.getString(1)
                    tableHeader.type = c.getString(2)
                    tableHeader.notnull = c.getInt(3)
                    tableHeader.dflt_value = c.getStringOrNull(4)
                    tableHeader.pk = c.getInt(5)

                    r.add(tableHeader)
                    c.moveToNext()
                }
            }
            r
        }
    }

    fun newRecord(): Record {
        return Record(db, this)
    }

    fun select(): List<Record> {
        if (!db.db.isOpen) return emptyList()

        val c: Cursor = db.db.rawQuery("SELECT * FROM $tableName", null)

        val r: ArrayList<Record> = ArrayList()

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                r.add(getRecordFromCursor(c))
                c.moveToNext()
            }
        }
        return r
    }

    private fun getRecordFromCursor(c: Cursor): Record  {

        val record = Record(db, this)
        record.isNewRecord = false

        headers.forEachIndexed { index, it ->
            val v = when (it.type.lowercase()) {
                "text" -> c.getString(index)
                "integer" -> c.getInt(index)
                else -> "Unknown"
            }
            record.values[it.name] = v
        }

        return record
    }

    fun create(callback: (ArrayList<TableHeader>) -> ArrayList<TableHeader>) {
        val sb = StringBuilder()
        sb.append("create table if not exists ")
        sb.append(tableName)
        sb.append(" (")

        val list = callback(ArrayList<TableHeader>())
        list.forEach {
            sb.append(it.name + " ")
            sb.append(it.type + " ")
            if (it.pk == 1) {
                sb.append(" PRIMARY KEY ")
            }
            if (list.last() != it) sb.append(", ")
        }

        sb.append(");")

        db.db.execSQL(sb.toString())

    }
}
