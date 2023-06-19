package com.pine.lib.addone.db

import android.database.Cursor
import androidx.core.database.*
import com.pine.lib.debug.e

data class Records(
    var dbName: String = "",
    var tableName: String? = null,
    var sql: String? = null,
    var headers: ArrayList<TableHeader> = ArrayList(),
    var records: ArrayList<Record> = ArrayList(),
) {
    fun initHeadersBaseARecord(dbName: String, tableName: String?, c: Cursor) {
        this.dbName = dbName
        this.tableName = tableName
        this.headers = ArrayList()

        (0 until c.columnCount).forEach {
            val th = TableHeader()
            th.name = c.getColumnName(it)
            th.type = when (c.getType(it)) {
                Cursor.FIELD_TYPE_BLOB -> "blob"
                Cursor.FIELD_TYPE_FLOAT -> "double"
                Cursor.FIELD_TYPE_INTEGER -> "integer"
                Cursor.FIELD_TYPE_STRING -> "text"
                Cursor.FIELD_TYPE_NULL -> "null"
                else -> {
                    e("Unknown Type ${c.getType(it)}")
                    "unknown"
                }
            }

            headers.add(th)
        }

    }

    fun anylizeLine(c: Cursor, pk: String? = null) {
        val record = Record(dbName, tableName)
        record.isNewRecord = false
        record.pk = pk

        headers.forEachIndexed { index, it ->
            val v = when (it.type.lowercase()) {
                "blob" -> c.getBlobOrNull(index)
                "float" -> c.getFloatOrNull(index)
                "real" -> c.getFloatOrNull(index)
                "double" -> c.getDoubleOrNull(index)
                "integer" -> c.getLongOrNull(index)
                "short" -> c.getShortOrNull(index)
                "long" -> c.getLongOrNull(index)
                "text" -> c.getStringOrNull(index)
                "null" -> c.getStringOrNull(index)
                else -> {
                    e("Unknown Type ${it.type}")
                    "Unknown"
                }
            }
            record.values[it.name] = v
        }

        records.add(record)
    }

}