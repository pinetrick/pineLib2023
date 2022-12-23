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
        structure = columnDefinitions.map { columnDefinition ->
            val columnRegex = """
      (?i)(\w+)\s+(\w+)(?:\s+(PRIMARY\s+KEY))?(?:\s+(NOT\s+NULL))?(?:\s+DEFAULT\s+(\w+))?
    """.trimIndent().toRegex()
            val columnMatchResult = columnRegex.find(columnDefinition)
            val (name, type, primaryKey, notNull, defaultValue) = columnMatchResult!!.destructured
            Column(
                name = name,
                type = type,
                primaryKey = primaryKey.isNotEmpty(),
                notNull = notNull.isNotEmpty(),
                defaultValue = defaultValue.ifEmpty { null }
            )
        }
    }

    data class Column(
        val name: String,
        val type: String,
        val primaryKey: Boolean,
        val notNull: Boolean,
        val defaultValue: String?
    )
}