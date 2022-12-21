package com.pine.lib.provider.opr

import android.content.Intent
import android.net.Uri
import com.pine.lib.addone.DataCleanManager
import com.pine.lib.addone.MyTimer
import com.pine.lib.app.c
import com.pine.lib.app.intent
import com.pine.lib.provider.server_socket.RequestData
import com.pine.lib.provider.server_socket.ResponseData
import com.pine.lib.view.db.db_choose.DbChooseActivity
import kotlin.system.exitProcess

class Header: BaseOpr() {


    fun startDbBrowser() {
        intent(DbChooseActivity::class)
        return ret()
    }

    fun exitApplication() {
        MyTimer().setInterval(100).setOnTimerListener {
            exitProcess(0)
        }.start(1)

        return ret()
    }

    fun deleteAllData() {
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

    fun uninstallApplication() {
        val intent = Intent()
        intent.action = Intent.ACTION_DELETE
        intent.data = Uri
            .parse("package:" + c().packageName)
        c().startActivity(intent)

        return ret()
    }

    // -------- PublicReturn --------
    private fun ret(isSuccess: Boolean = true) {
        responseData.content = if (isSuccess) "Done"
        else "Error"
    }
}