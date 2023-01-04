package com.pine.lib.addone.db

class CreateTable {
    var structure: List<Column>
    var sql: String

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
            Column(
                cid = cid++,
                name = name,
                type = type,
                pk = if (primaryKey.isEmpty()) 0 else 1,
                notnull = notNull.isNotEmpty(),
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

    data class Column(
        val cid: Int,
        val name: String,
        val type: String,
        val pk: Int,
        val notnull: Boolean,
        val dflt_value: String?
    )
}