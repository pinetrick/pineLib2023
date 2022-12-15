package com.pine.lib.addone.db

import android.database.Cursor

class Table constructor(val db: Db, val tableName: String) {


    val headers: ArrayList<Pair<String, String>> by lazy {
        if (!db.db.isOpen) {
            ArrayList()
        } else {
            val c: Cursor = db.db.rawQuery("pragma table_info ('$tableName');", null)

            val r: ArrayList<Pair<String, String>> = ArrayList<Pair<String, String>>()

            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    r.add(Pair(c.getString(1), c.getString(2)))
                    c.moveToNext()
                }
            }
            r
        }

    }

    fun select(): List<HashMap<String, Any>> {
        if (!db.db.isOpen) return emptyList()

        val c: Cursor = db.db.rawQuery("SELECT * FROM $tableName", null)

        val r: ArrayList<HashMap<String, Any>> = ArrayList<HashMap<String, Any>>()

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                r.add(getRecordFromCursor(c))
                c.moveToNext()
            }
        }
        return r
    }

    private fun getRecordFromCursor(c: Cursor): HashMap<String, Any>  {

        var values: HashMap<String, Any> = HashMap<String, Any>()

        headers.forEachIndexed { index, it ->
            val v = when (it.second.lowercase()) {
                "text" -> c.getString(index)
                "integer" -> c.getInt(index)
                else -> "Unknown"
            }
            values[it.first] = v
        }

        return values
    }

    fun create(callback: (ArrayList<Pair<String, String>>) -> ArrayList<Pair<String, String>>) {
        val sb = StringBuilder()
        sb.append("create table if not exists ")
        sb.append(tableName)
        sb.append(" (")


        val list = callback(ArrayList<Pair<String, String>>())
        list.forEach {
            sb.append(it.first)
            sb.append(" " + it.second)
            if (list.last() != it) sb.append(", ")
        }

        sb.append(");")

        db.db.execSQL(sb.toString())

    }
}

class HeaderBuilder {

}