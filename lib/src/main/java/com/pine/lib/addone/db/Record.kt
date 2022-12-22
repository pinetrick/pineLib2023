package com.pine.lib.addone.db

class Record constructor(val dbName: String, val tableName: String?) {

    var pk: String? = null
    var isNewRecord = true
    var values: MutableMap<String, Any?> = mutableMapOf()

    fun save(): Record {
        val db = Db.getDb(dbName)
        val table = db.model(tableName!!)

        if (!db.isOpen()) return this

        val sql: Pair<String, Array<Any?>> = if (isNewRecord) {
            getInsertSql()
        } else {
            val pk = table.headers.firstOrNull { it.pk == 1 }
            if (pk == null) {
                getInsertSql()
            } else {
                getUpdateSql(pk)
            }

        }
        db.execSQL(sql.first, sql.second)
        return this
    }

    private fun getUpdateSql(pk: TableHeader): Pair<String, Array<Any?>> {
        val db = Db.getDb(dbName)
        val table = db.model(tableName!!)
        val sb = StringBuilder()
        val array: ArrayList<Any?> = ArrayList()

        sb.appendLine("UPDATE [$tableName]")
        sb.append("SET ")

        table.headers.forEach {
            if (it.pk != 1) {
                sb.appendLine("[${it.name}] = ?,")
                array.add(values[it.name])
            }
        }
        sb.setLength(sb.length - 2)
        sb.appendLine()

        sb.appendLine(" WHERE [${pk.name}] = ?;")
        array.add(values[pk.name])


        return Pair(sb.toString(), array.toArray())
    }


    private fun getInsertSql(): Pair<String, Array<Any?>> {
        val sb = StringBuilder()
        val array: ArrayList<Any?> = ArrayList()

        sb.append("INSERT INTO [${tableName}] (")

        values.keys.forEach {
            sb.append("[$it], ")
        }

        sb.setLength(sb.length - 2)
        sb.append(") VALUES (")
        values.values.forEach {
            sb.append("?, ")
            array.add(it)
        }
        sb.setLength(sb.length - 2)
        sb.append(");")

        return Pair(sb.toString(), array.toArray())
    }

    operator fun set(name: String, value: Any?): Record {
        values[name] = value
        return this
    }

    operator fun get(name: String): Any? {
        return values[name]
    }
}