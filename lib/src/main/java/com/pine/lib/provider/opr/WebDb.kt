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
        val tableStructure = Db(requestData.urls[2])
            .recordsFromRawQuery("pragma table_info ('${requestData.urls[3]}');")

        tableStructure.tableName = "[STRUCTURE]" + requestData.urls[3]
        tableStructure.headers[0].pk = 1
        tableStructure.records.map {
            it.pk = "cid"
        }

        responseData.returnObj = tableStructure
    }

    fun update() {
        val db = requestData.urls[2]
        var table = requestData.bodyArgs["tableName"] ?: ""
        val isStructureChange = table.startsWith("[STRUCTURE]")
        if (isStructureChange) table = table.substring(11)
        val key = requestData.bodyArgs["key"]
        val newValue = requestData.bodyArgs["newValue"]!!
        val pkValue = requestData.bodyArgs["pkValue"]

        if (!isStructureChange) {
            Db(db).model(table).find(pkValue)?.let { record ->
                record.values[key!!] = newValue
                record.save()
            }
            return success()
        }

        //Structure change
        if (key == "cid") return error("You Cannot Change CID")

        val oldStructure = Db(requestData.urls[2])
            .recordsFromRawQuery("pragma table_info ('$table')")

        if (key == "name") {
            val oldKey = oldStructure.records.find {
                it["cid"].toString() == pkValue
            }!!
            val cid = oldKey["cid"] as Int
            val name = oldKey["name"]
            val type = oldKey["type"] as String
            val notnull = oldKey["notnull"] == 1
            val dflt_value = if (oldKey["dflt_value"] != null) "DEFAULT ${oldKey["dflt_value"]}" else ""
            val pk = oldKey["pk"]



            //var sql = "ALTER TABLE [$table] ADD COLUMN [$newValue] $type $notnull $dflt_value;"
            Db(db).model(table).alterColumn(cid, newValue, type, notnull, dflt_value)
//
//            sql = "UPDATE [$table] SET [$newValue] = [$name]"
//            Db(db).execSQL(sql)
//
//            sql = "ALTER TABLE [$table] DROP COLUMN [$name]"
//            Db(db).execSQL(sql)

            responseData.returnObj = "Success"
        }


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