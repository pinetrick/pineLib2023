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

    fun deleteTable() {
        responseData.returnObj = Db(requestData.urls[2]).deleteTable(requestData.urls[3])

    }

    fun select() {
        responseData.returnObj =
            Db(requestData.urls[2]).model(requestData.urls[3]).limit(100).run {
                order("$pk DESC")
            }.select()
    }

    fun structure() {
        val db = requestData.urls[2]
        val table = requestData.urls[3]

        return getStructure(db, table)
    }

    private fun getStructure(db: String, table: String){
        val tableStructure = Db(db)
            .recordsFromRawQuery("pragma table_info ([${table}]);")

        tableStructure.tableName = "[STRUCTURE]$table"
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
        val newValue = requestData.bodyArgs["newValue"]
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
            .recordsFromRawQuery("pragma table_info ([$table])")


        val oldKey = oldStructure.records.find {
            it["cid"].toString() == pkValue
        }!! //type
        val cid = oldKey["cid"] as Int
        var name = oldKey["name"] as String
        var type = oldKey["type"] as String
        var notnull = oldKey["notnull"] == 1
        var dflt_value = oldKey["dflt_value"] as String?
        var pk = oldKey["pk"] == 1

        if (key == "name") name = newValue!!
        if (key == "type") type = newValue!!
        if (key == "notnull") notnull = newValue == "1"
        if (key == "dflt_value") dflt_value = newValue
        if (key == "pk") pk = newValue == "1"

        Db(db).model(table).alterColumn(cid, name, type, notnull, dflt_value, pk)

        return success()

    }

    fun delete(){
        val db = requestData.urls[2]
        var table = requestData.urls[3]
        val isStructureChange = table.startsWith("[STRUCTURE]")
        if (isStructureChange) table = table.substring(11)
        val pks = requestData.bodyArgs["pk"]!!.split(",")

        if (!isStructureChange) {
            pks.forEach {
                Db(db).model(table).find(it)?.delete()
            }
            return success()
        }

        Db(db).model(table).deleteColumn(pks)
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
        val type = requestData.bodyArgs["2"] ?: "TEXT"
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
        if (sql.trim().lowercase().startsWith("pragma table_info")) {
            val db = requestData.urls[2]
            val table = Db(db).getSingleTableName(sql)
            return getStructure(db, table!!)
        }

        responseData.returnObj = Db(requestData.urls[2]).recordsFromRawQuery(sql)
    }

    data class DbWithTable(
        val dbName: String,
        val tables: List<String>,
    )
}