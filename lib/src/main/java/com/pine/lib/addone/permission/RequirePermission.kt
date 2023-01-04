package com.pine.lib.addone.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.pine.lib.app.a
import com.pine.lib.debug.e

object RequirePermission {
    fun require(vararg permissions: PermissionList) {

        val needRequire: ArrayList<String> = ArrayList()

        permissions.forEach {
            val permission = ActivityCompat.checkSelfPermission(a(), it.code)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                needRequire.add(it.code)
            }
        }

        if (needRequire.isNotEmpty()) {
            try {
                ActivityCompat.requestPermissions(
                    a(),
                    needRequire.toTypedArray(),
                    682001
                )
            } catch (ee: Exception) {
                e(ee)
            }
        }

    }

}

enum class PermissionList(val code: String) {
    READ_EXTERNAL_STORAGE("android.permission.READ_EXTERNAL_STORAGE"),
    WRITE_EXTERNAL_STORAGE("android.permission.WRITE_EXTERNAL_STORAGE")
}