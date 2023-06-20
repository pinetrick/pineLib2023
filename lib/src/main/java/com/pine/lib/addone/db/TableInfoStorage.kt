package com.pine.lib.addone.db

object TableInfoStorage {

    fun getHeaders(dbName: String, tableName: String): ArrayList<TableHeader> {
        val key = "$dbName - $tableName"
        if (headerCache.containsKey(key)) return headerCache[key]!!


        val db = Db.getDb(dbName)
        val records: Records = db.recordsFromRawQuery("pragma table_info ([$tableName]);")

        val r: ArrayList<TableHeader> = ArrayList()

        records.records.forEach { c ->
            val tableHeader = TableHeader()
            tableHeader.cid = c.values["cid"] as Int
            tableHeader.name = c.values["name"] as String
            tableHeader.type = c.values["type"] as String
            tableHeader.notnull = c.values["notnull"] as Int
            tableHeader.dflt_value = c.values["dflt_value"] as String?
            tableHeader.pk = c.values["pk"] as Int

            r.add(tableHeader)
        }
        headerCache[key] = r
        return r
    }

    private val headerCache: HashMap<String, ArrayList<TableHeader>> = HashMap()

}