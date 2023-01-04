package com.pine.lib.debug

import android.os.Build
import com.pine.lib.addone.datetime.DateTime
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import kotlin.system.exitProcess


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

        db.model("crash").create {
            it.apply {
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("details", "TEXT"))
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
                    db.model("sql_history").newRecord().set("sql", sql)
                        .set("create_time", DateTime.now.toString()).save()
                }
            } finally {
                callFromLibDb = false
            }
        }
    }

    fun recordCrash(thread: Thread, throwable: Throwable) {
        val details = "Thread: ${thread.name}\n" +
                "Exception: ${throwable.javaClass.name}\n" +
                "Message: ${throwable.message}\n" +
                "Stack trace:\n${throwable.stackTrace.joinToString("\n")}"

        db.model("crash").newRecord()
            .set("details", details)
            .set("create_time", DateTime.now.toString())
            .save()

        e("Crash Application Will Exit!")
        exitProcess(0)
    }

}