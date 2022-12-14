package com.pine.lib.view.toast

import android.widget.Toast
import com.pine.lib.app.app
import com.pine.lib.app.stringResources


fun toast(id: Int){
    Toast.makeText(app(), stringResources(id), Toast.LENGTH_LONG).show()
}

fun toast(s: Any){
    Toast.makeText(app(), s.toString(), Toast.LENGTH_LONG).show()
}
