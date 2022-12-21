package com.pine.lib.addone.db

import android.database.Cursor
import androidx.core.database.getStringOrNull

class Table constructor(val dbName: String, val tableName: String? = null) {
    private val db: Db = Db.getDb(dbName)

    private val pk: String? by lazy {
        headers.firstOrNull { it.pk == 1 }?.name
    }

    private var orderBy: String? = null
    private var limit: String? = null
    private var where: ArrayList<String>? = null

    val headers: ArrayList<TableHeader> by lazy {
        if (!db.isOpen()) {
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
            c.close()
            r
        }
    }

    fun newRecord(): Record {
        return Record(dbName, tableName)
    }

    fun order(order: String): Table {
        this.orderBy = order
        return this
    }

    fun limit(limit: Int): Table {
        this.limit = limit.toString()
        return this
    }

    fun where(key: String, value: String): Table {
        return where("$key = $value")
    }

    fun where(condition: String): Table {
        if (this.where == null) {
            this.where = ArrayList()
        }
        this.where!!.add(condition)


        return this
    }

    fun select(): Records {
        if (!db.isOpen()) return Records()

        val sb = StringBuilder()
        sb.append("SELECT * FROM [$tableName] ")

        where?.let {
            sb.append(" WHERE ")
            it.forEach { condition ->
                sb.append(condition)
                if (it.last() != condition) sb.append(" AND ")
            }
        }
        orderBy?.let { sb.append(" ORDER BY $it ") }
        limit?.let { sb.append(" LIMIT $it ") }


        return db.recordsFromRawQuery(sb.toString())
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

        db.execSQL(sb.toString())

    }
}
