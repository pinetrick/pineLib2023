package com.pine.lib.addone.db

class Table constructor(val dbName: String, val tableName: String? = null) {
    private val db: Db = Db.getDb(dbName)

    val pk: String? by lazy {
        headers.firstOrNull { it.pk == 1 }?.name
    }

    private var orderBy: String? = null
    private var limit: String? = null
    private var where: ArrayList<String>? = null

    val headers: ArrayList<TableHeader> by lazy {
        val records: Records = db.recordsFromRawQuery("pragma table_info ('$tableName');")

        val r: ArrayList<TableHeader> = ArrayList<TableHeader>()

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

        r
    }

    fun newRecord(): Record {
        return Record(dbName, tableName)
    }

    fun order(order: String): Table {
        this.orderBy = order
        return this
    }

    fun limit(limit: Int): Table {
        this.limit = limit.toString()
        return this
    }

    fun where(key: String, value: String): Table {
        return where("[$key] = '$value'")
    }

    fun where(condition: String): Table {
        if (this.where == null) {
            this.where = ArrayList()
        }
        this.where!!.add(condition)


        return this
    }

    fun select(): Records {
        val sb = StringBuilder()
        sb.append("SELECT * FROM [$tableName] ")

        where?.let {
            sb.append(" WHERE ")
            it.forEach { condition ->
                sb.append(condition)
                if (it.last() != condition) sb.append(" AND ")
            }
        }
        orderBy?.let { sb.append(" ORDER BY $it ") }
        limit?.let { sb.append(" LIMIT $it ") }


        return db.recordsFromRawQuery(sb.toString())
    }

    fun find(): Record? {
        val results = limit(1).select()
        if (results.records.size == 0) return null

        return results.records[0]
    }


    fun create(callback: (ArrayList<TableHeader>) -> ArrayList<TableHeader>) {
        val sb = StringBuilder()
        sb.append("CREATE TABLE IF NOT EXISTS ")
        sb.append("[$tableName]")
        sb.append(" (")

        val list = callback(ArrayList<TableHeader>())
        list.forEach {
            sb.append("[" + it.name + "] ")
            sb.append(it.type + " ")
            if (it.pk == 1) {
                sb.append(" PRIMARY KEY ")
            }
            if (list.last() != it) sb.append(", ")
        }

        sb.append(");")

        db.execSQL(sb.toString())

    }
}
