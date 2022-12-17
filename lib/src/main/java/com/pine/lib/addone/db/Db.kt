package com.pine.lib.addone.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pine.lib.app.c
import com.pine.lib.debug.e
import com.pine.lib.view.toast.toast


class Db {
    var dbName: String
    var lastSql: String = ""
    var db: SQLiteDatabase

    constructor(dbName: String) {
        this.dbName = dbName
        db = c().openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null)

    }


    fun model(tableName: String): Table {
        return Table(this, tableName)
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
        return r.reversed()
    }

    fun rawQuery(sql: String, selectionArgs: Array<String>? = null): Cursor {
        logSql(sql, selectionArgs as Array<Any?>?)
        return db.rawQuery(sql, selectionArgs)
    }

    fun execSQL(sql: String, bindArgs: Array<Any?>) {
        logSql(sql, bindArgs)
        return db.execSQL(sql, bindArgs)
    }

    private fun logSql(sql: String, bindArgs: Array<Any?>? = null) {
        lastSql = sql
        bindArgs?.forEach {
            lastSql = lastSql.replaceFirst("?", "'$it'")
        }
        toast(lastSql)
        e(lastSql)
    }

    companion object {
        fun getAllDb(): List<String> {
            return c().databaseList().toList().filter {
                !it.contains("journal")
            }.reversed()
        }
    }
}