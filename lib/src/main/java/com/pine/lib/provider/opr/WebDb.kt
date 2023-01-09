package com.pine.lib.provider.opr

import com.pine.lib.addone.db.CreateTable
import com.pine.lib.addone.db.Db
import java.net.URLDecoder


class WebDb : BaseOpr() {

    fun listDb() {
        val db = Db.getAllDb()
        val r = ArrayList<DbWithTable>()
        db.forEach {
            r.add(DbWithTable(it, Db(it).tables()))
        }

        responseData.returnObj = r
    }

    fun listTable() {
        responseData.returnObj = Db(requestData.urls[2]).tables()

    }

    fun select() {
        responseData.returnObj =
            Db(requestData.urls[2]).model(requestData.urls[3]).limit(100).run {
                order("$pk DESC")
            }.select()
    }

    fun structure() {
        val tableCreateSql = Db(requestData.urls[2])
            .model("sqlite_master")
            .where("type", "table")
            .where("tbl_name", requestData.urls[3])
            .find()?.let {
                it["sql"]
            }



        responseData.returnObj = CreateTable(tableCreateSql!! as String).toRecords(requestData.urls[2])
    }

    fun exec() {
        val sql = URLDecoder.decode(requestData.urls[3], "UTF-8")
        responseData.returnObj = Db(requestData.urls[2]).execSQL(sql)
    }

    fun query() {
        val sql = URLDecoder.decode(requestData.urls[3], "UTF-8")
        responseData.returnObj = Db(requestData.urls[2]).recordsFromRawQuery(sql)
    }

    data class DbWithTable(
        val dbName: String,
        val tables: List<String>,
    )
}