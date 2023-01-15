package com.pine.lib.provider.background_task

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.pine.lib.BuildConfig
import com.pine.lib.app.C
import com.pine.lib.app.StaticPineApplication
import com.pine.lib.provider.server_socket.ClientServer

class BackgroundTaskProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        StaticPineApplication.onCreate(context as Application)
        if (C.isAppInDebugModel) {
            ClientServer(8080).start()
        }


        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return null
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }


}