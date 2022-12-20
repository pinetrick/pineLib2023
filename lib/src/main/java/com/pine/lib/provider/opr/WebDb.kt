package com.pine.lib.provider.opr

import com.pine.lib.addone.db.Db
import com.pine.lib.app.gson
import java.net.URLDecoder


class WebDb {

    fun run(route: List<String>): String {
        try {
            val func = this::class.java.getDeclaredMethod(route[1], List::class.java)

            return func.invoke(this, route) as String
        }
        catch (e: Exception) {
            return e.stackTraceToString().replace("\r\n", "<br>")
        }

    }

    fun listDb(route: List<String>): String {
        val db = Db.getAllDb()
        val r = ArrayList<DbWithTable>()
        db.forEach {
            r.add(DbWithTable(it, Db(it).tables()))
        }

        return gson().toJson(r)
    }

    fun listTable(route: List<String>): String {
        val tables = Db(route[2]).tables()
        return gson().toJson(tables)
    }

    fun select(route: List<String>): String {
        val data = Db(route[2]).model(route[3]).select()
        return gson().toJson(data)
    }

    fun exec(route: List<String>): String {
        val sql = URLDecoder.decode(route[3], "UTF-8")
        val data = Db(route[2]).execSQL(sql)

        return gson().toJson(data)
    }

    fun query(route: List<String>): String {
        val sql = URLDecoder.decode(route[3], "UTF-8")
        val data = Db(route[2]).recordsFromRawQuery(sql)

        return gson().toJson(data)
    }

    data class DbWithTable(
        val dbName: String,
        val tables: List<String>,
    )
}