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


        val oldKey = oldStructure.records.find {
            it["cid"].toString() == pkValue
        }!! //type
        val cid = oldKey["cid"] as Int
        var name = oldKey["name"] as String
        var type = oldKey["type"] as String
        var notnull = oldKey["notnull"] == 1
        var dflt_value = if (oldKey["dflt_value"] != null) "DEFAULT ${oldKey["dflt_value"]}" else ""
        var pk = oldKey["pk"] == 1

        if (key == "name") name = newValue
        if (key == "type") type = newValue
        if (key == "notnull") notnull = newValue == "1"
        if (key == "dflt_value") dflt_value = newValue
        if (key == "pk") pk = newValue == "1"

        Db(db).model(table).alterColumn(cid, name, type, notnull, dflt_value, pk)

        return success()

    }

    fun newRecord(){
        val db = requestData.urls[2]
        var table = requestData.urls[3]
        val isStructureChange = table.startsWith("[STRUCTURE]")
        if (isStructureChange) table = table.substring(11)

        if (!isStructureChange) {
            val record = Db(db).model(table).newRecord()
            val headers = Db(db).model(table).headers

            headers.forEach{
                val index = headers.indexOf(it)
                record[it.name] = requestData.bodyArgs[index.toString()] ?: it.dflt_value
            }
            record.save()


            return success()
        }

        val cid = requestData.bodyArgs["0"]?.toInt() ?: -1
        val name = requestData.bodyArgs["1"]!!
        val type = requestData.bodyArgs["2"]!!
        val notnull = requestData.bodyArgs["3"]?.toInt() ?: 0
        val dflt_value = requestData.bodyArgs["4"] ?: null
        val pk = requestData.bodyArgs["5"]?.toInt() ?: 0

        Db(db).model(table).addColumn(name, type, cid, notnull == 1, dflt_value)

        return success()
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