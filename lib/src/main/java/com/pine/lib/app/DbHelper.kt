package com.pine.lib.app

import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.Table

fun db(dbName: String = "LibDefaultDb"): Db {
    return Db(dbName)
}

fun model(tableName: String, dbName: String = "LibDefaultDb"): Table {
    return db(dbName).model(tableName)
}