package com.pine.lib.addone.db

import java.util.*
import kotlin.collections.ArrayList

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

    fun where(key: String, value: Any?): Table {
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

    fun find(pkValue: Any? = null): Record? {
        if (pkValue != null) {
            where(pk!!, pkValue)
        }
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


    fun addColumn(
        name: String,
        type: String,
        location: Int = -1,
        notNull: Boolean = false,
        defValue: String? = null,
        isPk: Boolean = false
    ) {
        val header = headers.clone() as ArrayList<TableHeader>
        val _location = if (location == -1) header.size else location

        val tableHeader = TableHeader()
        tableHeader.cid = location
        tableHeader.name = name
        tableHeader.type = type
        tableHeader.notnull = if (notNull) 1 else 0
        tableHeader.dflt_value = defValue
        tableHeader.pk = if (isPk) 1 else 0
        header.add(_location, tableHeader)

        //创建临时表
        val tmpTableName = tableName!! + "-" + Random().nextInt(999999).toString()
        var sql = CreateTable(tmpTableName, header).sql
        db.execSQL(sql)

        //拷贝数据
        val sb = StringBuilder()
        sb.append("INSERT INTO [$tmpTableName] SELECT ")
        header.forEach {
            if (it.name == name) {
                sb.append("'$defValue'")
            } else {
                sb.append(it.name)
            }
            if (header.last() != it) sb.append(", ")
        }
        sb.append(" FROM [$tableName]")
        db.execSQL(sb.toString())

        //删除原始表
        db.execSQL("DROP TABLE [$tableName]")

        //重命名表
        db.execSQL("ALTER TABLE [$tmpTableName] RENAME TO [$tableName]")
    }

    fun alterColumn(
        location: Int,
        name: String? = null,
        type: String? = null,
        notNull: Boolean = false,
        defValue: String? = null,
        isPk: Boolean = false
    ) {
        val header = headers.clone() as ArrayList<TableHeader>
        val oldValue = header[location]

        val tableHeader = TableHeader()
        tableHeader.cid = location
        tableHeader.name = name ?: oldValue.name
        tableHeader.type = type ?: oldValue.type
        tableHeader.notnull = if (notNull) 1 else 0
        tableHeader.dflt_value = defValue
        tableHeader.pk = if (isPk) 1 else 0

        header[location] = tableHeader

        //创建临时表
        val tmpTableName = tableName!! + "-" + Random().nextInt(999999).toString()
        var sql = CreateTable(tmpTableName, header).sql
        db.execSQL(sql)

        //拷贝数据
        val sb = StringBuilder()
        sb.append("INSERT INTO [$tmpTableName] SELECT ")
        headers.forEach {
            sb.append(it.name)
            if (headers.last() != it) sb.append(", ")
        }
        sb.append(" FROM [$tableName]")
        db.execSQL(sb.toString())

        //删除原始表
        db.execSQL("DROP TABLE [$tableName]")

        //重命名表
        db.execSQL("ALTER TABLE [$tmpTableName] RENAME TO [$tableName]")
    }
}
