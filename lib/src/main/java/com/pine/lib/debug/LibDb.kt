package com.pine.lib.debug

import android.os.Build
import com.pine.lib.addone.datetime.DateTime
import com.pine.lib.addone.db.Db
import com.pine.lib.addone.db.TableHeader
import com.pine.lib.app.c
import com.pine.lib.file.PineFileReader
import com.pine.lib.file.PineFileWriter
import kotlin.system.exitProcess


var libDb: LibDb? = null

class LibDb() {

    var db = Db.getDb("LibDb")

    init {
        db.model("crash").create {
            it.apply {
                add(TableHeader("id", "INTEGER", pk = 1))
                add(TableHeader("details", "TEXT"))
                add(TableHeader("create_time", "TEXT"))
            }
        }
        libDb = this
    }


    fun recordSql(db: String, sql: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val filename = c().filesDir.toString() +  "/sql_history.txt"
            val exist = PineFileReader.read(filename) ?: ""
            val writeSql = "||NEW_SQL||" + DateTime.now.ToString() + "||" + db + "||" + sql + "\r\n"
            PineFileWriter.writeToFile(filename , writeSql + exist)
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