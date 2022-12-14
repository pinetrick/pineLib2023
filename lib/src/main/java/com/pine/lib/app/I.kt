package com.pine.lib.app

import android.content.Intent
import com.pine.lib.view.toast.toast
import kotlin.reflect.KClass

fun i(className: KClass<*>): I {
    return  I(className);
}


class I(cls: KClass<*>){

    var intent: Intent = Intent();

    init{
        intent.setClass(a(), cls.java);
    }



    fun show(): I{
        try {
            a().startActivity(intent);
        }
        catch (e: Exception){
            toast("Please Add Activity to Manifests")
        }
        return  this;



    }




    fun putExtra(name: String, value: String): I{
        intent.putExtra(name, value);
        return this
    }
    fun putExtra(name: String, value: Int): I{
        intent.putExtra(name, value);
        return this
    }
    fun putExtra(name: String, value: Double): I{
        intent.putExtra(name, value);
        return this
    }
    fun putExtra(name: String, value: Boolean): I{
        intent.putExtra(name, value);
        return this
    }

}
