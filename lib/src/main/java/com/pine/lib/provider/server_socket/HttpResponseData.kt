package com.pine.lib.provider.server_socket

import android.content.Intent
import android.net.Uri
import com.pine.lib.addone.DataCleanManager
import com.pine.lib.addone.MyTimer
import com.pine.lib.app.a
import com.pine.lib.app.c
import com.pine.lib.app.intent
import com.pine.lib.file.AssetsHelper
import com.pine.lib.view.db.db_choose.DbChooseActivity
import com.pine.lib.view.toast.toast
import kotlin.system.exitProcess

class HttpResponseData {

    fun getResponse(route: String): ByteArray? {
        val opr = route.split("/")
        return try {
            val func = this::class.java.getDeclaredMethod(opr[0], List::class.java)
            val res = func.invoke(this, opr) as String

            res.toByteArray()
        } catch (e: Exception) {
            toast(e)
            index(opr).toByteArray()
        }


    }

    fun index(opr: List<String>): String {
        return AssetsHelper.read("html/index.html")
    }

    // ---------- SYSTEM --------------
    fun db(opr: List<String>): String {
        intent(DbChooseActivity::class)
        return ret()
    }

    fun exit(opr: List<String>): String {
        MyTimer().setInterval(100).setOnTimerListener {
            exitProcess(0)
        }.start(1)

        return ret()
    }

    fun deleteAllData(opr: List<String>): String {
        MyTimer().setInterval(100).setOnTimerListener {
            DataCleanManager.cleanInternalCache(c())
            DataCleanManager.cleanDatabases(c())
            DataCleanManager.cleanSharedPreference(c())
            DataCleanManager.cleanFiles(c())
            DataCleanManager.cleanExternalCache(c())
            exitProcess(0)
        }.start(1)

        return ret()

    }

    fun uninstall(opr: List<String>): String {
        val intent = Intent()
        intent.action = Intent.ACTION_DELETE
        intent.data = Uri
            .parse("package:" + c().packageName)
        c().startActivity(intent)

        return ret()
    }

    // -------- PublicReturn --------
    private fun ret(isSuccess: Boolean = true): String {
        return if (isSuccess) "Done"
        else "Error"
    }
}