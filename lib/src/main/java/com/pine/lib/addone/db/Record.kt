package com.pine.lib.addone.db

class Record constructor(val db: Db, val table: Table) {

    var isNewRecord = true
    var values: HashMap<String, Any> = HashMap()

    fun save() {
        if (!db.db.isOpen) return

        var sql = ""
        if (isNewRecord) {
            sql = getInsertSql()
        } else {
            val pk = table.headers.first { it.pk == 1 }
            sql = if (pk == null) {
                getInsertSql()
            } else {
                getUpdateSql(pk)
            }

        }
        db.db.execSQL(sql)
    }

    private fun getUpdateSql(pk: TableHeader): String {
        return ""
    }


    fun getInsertSql(): String {
        val sb = StringBuilder()
        sb.append("INSERT INTO ${table.tableName} (")

        values.keys.forEach {
            sb.append("'$it', ")
        }

        sb.setLength(sb.length - 2)
        sb.append(") VALUES (")
        values.values.forEach {
            sb.append("'$it', ")
        }
        sb.setLength(sb.length - 2)
        sb.append(");")

        return sb.toString()
    }

    fun put(name: String, value: Any): Record {
        values[name] = value
        return this
    }
}