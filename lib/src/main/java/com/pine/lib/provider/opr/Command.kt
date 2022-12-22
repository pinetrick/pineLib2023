package com.pine.lib.provider.opr

import com.pine.lib.addone.app_broadcast.AppBroadcastController

class Command : BaseOpr() {
    fun run(){
        AppBroadcastController.reg("lib_command") { key, value ->
//            val className = Class.forName("nz.co.smartpay.artoo.ui.home.HomeScreenVM") as Class<ViewModel>
//            val viewModel = ViewModelProvider(owner = a() as ViewModelStoreOwner)[className]
//            val method: Method = className.getMethod("onValueAppended", String::class.java)
//            method.invoke("4")
        }
        AppBroadcastController.send("lib_command")
    }
}