package com.pine.lib.addone.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pine.lib.app.c
import com.pine.lib.debug.libDb


class Db(var dbName: String) {
    var lastSql: String = ""
    private val tablesMap: HashMap<String, Table> = HashMap()

    private fun<T> useDb(dbCalls: (SQLiteDatabase) -> T): T{
        val db = c().openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null)
        db.use { db ->
            return dbCalls(db)
        }
    }

    fun model(tableName: String): Table {
        if (tablesMap[tableName] == null) {
            tablesMap[tableName] = Table(dbName, tableName)
        }
        return tablesMap[tableName]!!
    }

    fun tables(): List<String> {
        return useDb { db ->
            val c: Cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

            val r: ArrayList<String> = ArrayList<String>()

            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    r.add(c.getString(0))
                    c.moveToNext()
                }
            }
            c.close()

            r.reversed()
        }

    }

    @Suppress("UNCHECKED_CAST")
    private fun rawQuery(sql: String, selectionArgs: Array<String>? = null): Cursor {
        logSql(sql, selectionArgs as Array<Any?>?)
        return useDb { it.rawQuery(sql, selectionArgs) }
    }

    fun recordsFromRawQuery(sql: String, selectionArgs: Array<String>? = null): Records {
        return useDb { db->
            val records = Records()
            val c = db.rawQuery(sql, selectionArgs)
            logSql(sql, selectionArgs as Array<Any?>?)
            c.use { c ->
                records.sql = lastSql

                val singleTableName = getSingleTableName(sql)
                val pk = getPrimaryKey(sql)

                if (c.moveToFirst()) {
                    while (!c.isAfterLast) {
                        if (records.dbName.isEmpty()) {
                            records.initHeadersBaseARecord(dbName, singleTableName, c)
                        }
                        records.anylizeLine(c, pk)
                        c.moveToNext()
                    }
                }
            }
            records
        }
    }

    fun getSingleTableName(sql: String): String? {
        // ????????????????????????????????????
        val pattern = "(?i)FROM\\s+(\\[?\\S+\\]?)".toRegex()
        val matchResult = pattern.find(sql)
        return if (matchResult != null) {
            // ?????????????????????????????????
            matchResult.groupValues[1].replace("[", "").replace("]", "")
        } else {
            // ???????????? null
            null
        }
    }

    fun getPrimaryKey(sql: String): String? {
        // ????????????????????????????????????
        val pattern = "(?i)FROM\\s+(\\[?\\S+\\]?)".toRegex()
        val tableName = pattern.find(sql)?.groupValues?.get(1)
        if (tableName == null) {
            // ??????????????????????????????????????????????????????
            return null
        }

        // ??????????????????
        return useDb { db ->
            val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)
            val pkColumnIndex = cursor.getColumnIndexOrThrow("pk")
            var primaryKey: String? = null
            while (cursor.moveToNext()) {
                if (cursor.getInt(pkColumnIndex) == 1) {
                    // ?????????????????????????????????
                    val nameColumnIndex = cursor.getColumnIndexOrThrow("name")
                    primaryKey = cursor.getString(nameColumnIndex)
                    break
                }
            }
            cursor.close()
            primaryKey
        }
    }

    fun execSQL(sql: String, bindArgs: Array<Any?> = emptyArray()) {
        logSql(sql, bindArgs)
        return useDb{ it.execSQL(sql, bindArgs) }
    }

    private fun logSql(sql: String, bindArgs: Array<Any?>? = null) {
        var _sql = sql
        bindArgs?.forEach {
            _sql = _sql.replaceFirst("?", "'$it'")
        }
        lastSql = _sql
        libDb?.recordSql(dbName, _sql)
    }

    companion object {
        private val dbs: HashMap<String, Db> = HashMap()

        fun getAllDb(): List<String> {
            return c().databaseList().toList().filter {
                !it.contains("journal")
            }.reversed()
        }

        fun getDb(dbName: String): Db {
            if (dbs[dbName] == null) {
                dbs[dbName] = Db(dbName)
            }
            return dbs[dbName]!!
        }

    }

}