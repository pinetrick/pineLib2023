package com.pine.lib.addone.db

import android.database.Cursor
import androidx.core.database.getStringOrNull

class Table constructor(val db: Db, val tableName: String) {

    val headers: ArrayList<TableHeader> by lazy {
        if (!db.db.isOpen) {
            ArrayList()
        } else {
            val c: Cursor = db.rawQuery("pragma table_info ('$tableName');")

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

    fun select(): Records {
        if (!db.db.isOpen) return Records()

        val c: Cursor = db.rawQuery("SELECT * FROM $tableName")

        val records = Records()

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                if (records.dbName.isEmpty()) {
                    records.initHeadersBaseARecord(db, this, c)
                }
                records.anylizeLine(c)
                c.moveToNext()
            }
        }
        return records
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
