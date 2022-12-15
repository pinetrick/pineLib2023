package com.pine.lib.app

import android.content.Intent
import com.pine.lib.view.toast.toast
import kotlin.reflect.KClass

fun intent(className: KClass<*>, params: ((Intent) -> Unit)? = null): Intent {
    val intent = Intent()
    intent.setClass(a(), className.java)
    params?.let {
        params(intent)
    }


    try {
        a().startActivity(intent)
    }
    catch (e: Exception){
        toast("Please Add Activity to Manifests")
    }
    return intent
}
