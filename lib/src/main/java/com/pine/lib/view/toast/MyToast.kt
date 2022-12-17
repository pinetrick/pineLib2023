package com.pine.lib.view.toast

import android.widget.Toast
import com.pine.lib.app.app
import com.pine.lib.app.stringResources


fun toast(id: Int) {
    try {
        Toast.makeText(app(), stringResources(id), Toast.LENGTH_LONG).show()
    } catch (_: Exception) {

    }
}

fun toast(s: Any) {
    try {
        Toast.makeText(app(), s.toString(), Toast.LENGTH_LONG).show()
    } catch (_: Exception) {

    }

}
