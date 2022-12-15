package com.pine.lib.addone.db

import android.database.Cursor

class Record constructor(val db: Db, val table: Table) {

    var isNewRecord = true
    var values: HashMap<String, Any> = HashMap()

    fun save() {
        if (!db.db.isOpen) return

        var sql = ""
        if (isNewRecord) {
            sql = getInsertSql()
        } else {
            val pk = table.headers.first { it.pk == 1 }
            if (pk == null) {
                sql = getInsertSql()
            } else {

            }

        }
        db.db.execSQL(sql)
    }


    fun getInsertSql(): String {
        val sb = StringBuilder()
        sb.append("INSERT INTO ${table.tableName} (")

        values.keys.forEach {
            sb.append("'$it'")
            if (values.keys.last() != it) sb.append(", ")
        }
        sb.append(") VALUES (")
        values.values.forEach {
            sb.append("'$it'")
            if (values.values.last() != it) sb.append(", ")
        }
        sb.append(");")

        return sb.toString()
    }

    fun put(name: String, value: Any): Record {
        values[name] = value
        return this
    }
}