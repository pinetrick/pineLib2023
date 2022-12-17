package com.pine.lib.addone.db

class Record constructor(val db: Db, val table: Table) {

    var isNewRecord = true
    var values: MutableMap<String, Any> = mutableMapOf()

    fun save(): Record {
        if (!db.db.isOpen) return this

        val sql: Pair<String, Array<Any?>> = if (isNewRecord) {
            getInsertSql()
        } else {
            val pk = table.headers.first { it.pk == 1 }
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
        val sb = StringBuilder()
        val array: ArrayList<Any?> = ArrayList()

        sb.appendLine("UPDATE [${table.tableName}]")
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

        sb.append("INSERT INTO [${table.tableName}] (")

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

    fun put(name: String, value: Any): Record {
        values[name] = value
        return this
    }
}