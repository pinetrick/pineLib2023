package com.pine.lib.addone.db

class CreateTable {
    var structure: List<TableHeader>
    var sql: String

    constructor(tableName: String, structure: List<TableHeader>) {
        this.structure = structure

        val sb = StringBuilder()
        sb.append("CREATE TABLE [$tableName] (")
        structure.forEach {
            sb.append("[${it.name}] ${it.type} ")
            if (it.notnull == 1) sb.append(" NOT NULL ")
            if (it.pk == 1) sb.append(" PRIMARY KEY AUTOINCREMENT ")
            if (it.dflt_value != null) {
                val def = it.dflt_value.toString()
                if (def.startsWith("'") && def.endsWith("'")) {
                    val newdef = def.substring(1, def.length - 1)
                    it.dflt_value = newdef
                }

                sb.append(" DEFAULT ")
                if (it.type.lowercase() == "text") sb.append("'" + it.dflt_value + "'")
                else sb.append(it.dflt_value)
            }
            if (structure.last() != it) sb.append(", \r\n")
        }
        sb.append(");")


        this.sql = sb.toString()
    }

    constructor(createTableStatement: String) {
        sql = createTableStatement
        val regex =
            """(?i)CREATE\s+TABLE\s+\w+\s*\((.+)\)""".trimIndent().toRegex(RegexOption.MULTILINE)
        val matchResult = regex.find(createTableStatement)?.groups?.get(1)
        val columnDefinitions = matchResult?.value?.split(",")?.map { it.trim() } ?: emptyList()
        var cid = 0
        structure = columnDefinitions.map { columnDefinition ->
            val columnRegex = """
      (?i)(\w+)\s+(\w+)(?:\s+(PRIMARY\s+KEY))?(?:\s+(NOT\s+NULL))?(?:\s+DEFAULT\s+(\w+))?
    """.trimIndent().toRegex()
            val columnMatchResult = columnRegex.find(columnDefinition)
            val (name, type, primaryKey, notNull, defaultValue) = columnMatchResult!!.destructured
            TableHeader(
                cid = cid++,
                name = name,
                type = type,
                pk = if (primaryKey.isEmpty()) 0 else 1,
                notnull = if (notNull.isNotEmpty()) 1 else 0,
                dflt_value = defaultValue.ifEmpty { null }
            )
        }
    }

    fun toRecords(dbName: String): Records {
        val r = Records()
        r.dbName = dbName

        r.headers = arrayListOf(
            TableHeader(name = "cid"),
            TableHeader(name = "name"),
            TableHeader(name = "type"),
            TableHeader(name = "pk"),
            TableHeader(name = "notnull"),
            TableHeader(name = "dflt_value")
        )
        r.records = ArrayList()

        structure.forEach {
            val record = Record(
                dbName = dbName,
                tableName = "sqlite_master"
            )

            record.isNewRecord = false
            record.values["cid"] = it.cid
            record.values["name"] = it.name
            record.values["type"] = it.type
            record.values["pk"] = it.pk
            record.values["notnull"] = it.notnull
            record.values["dflt_value"] = it.dflt_value

            r.records.add(record)
        }


        return r
    }


}