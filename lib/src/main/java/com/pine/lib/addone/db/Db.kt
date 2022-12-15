package com.pine.lib.addone.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pine.lib.app.c


class Db {
    var dbName: String

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
        return r
    }


    companion object {
        fun getAllDb(): List<String> {
            return c().databaseList().toList().filter {
                !it.contains("journal")
            }
        }
    }
}