package com.pine.lib.provider.opr

import com.pine.lib.app.c
import com.pine.lib.file.PineFileReader


class Sql : BaseOpr() {
    fun show() {
        val filename = c().filesDir.toString() +  "/sql_history.txt"
        val r: ArrayList<SqlBean> = ArrayList()

        val sqls = (PineFileReader.read(filename) ?: "").split("||NEW_SQL||")
        sqls.forEach {
            try{
                val time = it.substring(0, 19)
                val dbIndex = it.indexOf("||", 21)
                val db = it.substring(21, dbIndex)
                val sql = it.substring(dbIndex + 2).trim()
                val sqlBean = SqlBean(time, db, sql)
                r.add(sqlBean)
            }
            catch (e: Exception) {

            }

        }

        responseData.returnObj = r
    }
}

data class SqlBean(
    val time: String,
    val db: String,
    val sql: String,
)

