package com.pine.lib.debug

import android.os.Build
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import java.time.LocalDateTime


var libDb: LibDb? = null

class LibDb() {

    var db = Db.getDb("LibDb")
    var callFromLibDb = false

    init {
        db.model("sql_history").create {
            it.apply {
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("sql", "TEXT"))
                add(TableHeader("create_time", "TEXT"))
            }
        }
        libDb = this
    }


    fun recordSql(sql: String) {
        if (!callFromLibDb) {
            try {
                callFromLibDb = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    db.model("sql_history").newRecord().put("sql", sql)
                        .put("create_time", LocalDateTime.now().toString()).save()
                }
            }
            finally {

                callFromLibDb = false
            }


        }
    }
}