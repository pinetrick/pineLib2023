package com.pine.lib.addone.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pine.lib.app.c
import com.pine.lib.debug.libDb


class Db(var dbName: String) {
    var lastSql: String = ""
    private var db: SQLiteDatabase = c().openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null)

    fun isOpen(): Boolean {
        return db.isOpen
    }

    fun model(tableName: String): Table {
        return Table(dbName, tableName)
    }

    fun tables(): List<String> {
        if (!db.isOpen) return emptyList()

        val c: Cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        val r: ArrayList<String> = ArrayList<String>()

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                r.add(c.getString(0))
                c.moveToNext()
            }
        }
        c.close()

        return r.reversed()
    }

    @Suppress("UNCHECKED_CAST")
    fun rawQuery(sql: String, selectionArgs: Array<String>? = null): Cursor {
        logSql(sql, selectionArgs as Array<Any?>?)
        return db.rawQuery(sql, selectionArgs)
    }

    fun execSQL(sql: String, bindArgs: Array<Any?> = emptyArray()) {
        logSql(sql, bindArgs)
        return db.execSQL(sql, bindArgs)
    }

    private fun logSql(sql: String, bindArgs: Array<Any?>? = null) {
        lastSql = sql
        bindArgs?.forEach {
            lastSql = lastSql.replaceFirst("?", "'$it'")
        }
        libDb?.recordSql(lastSql)
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