package com.pine.lib.view.message_box

import com.pine.lib.R
import com.pine.lib.app.a
import com.pine.lib.app.stringResources


open class MessageBox {
    var messageBoxObj: MessageBoxObj = MessageBoxObj()

    constructor(){

    }

    fun show(): MessageBox{

        if (!MessageBoxManager.messageboxQueue.contains(this)){
            MessageBoxManager.messageboxQueue.add(this)
        }

        if (MessageBoxManager.messageBoxShowing == null) {
            messageBoxObj.dialog = MessageBoxView(a(), R.style.dialog, messageBoxObj, this)
            messageBoxObj.dialog!!.show()

            MessageBoxManager.messageBoxShowing = this
        }
        return this
    }

    fun setMessageBoxStyle(RLayout: Int): MessageBox{
        messageBoxObj.baseLayout = RLayout
        return this
    }

    fun setOnBtnClickListener(listener: (idFrom1: Int) -> Unit): MessageBox{
        messageBoxObj.callback = listener
        return this
    }

    fun setCancelable(cancelable: Boolean): MessageBox{
        messageBoxObj.cancelable = cancelable
        return this
    }

    fun show(id: Int, vararg btns: Int): MessageBox{
        var btns_m: Array<String> = arrayOf<String>()
        btns.forEach {
            btns_m = btns_m.plus(stringResources(it))
        }
        messageBoxObj.title = stringResources(id)
        messageBoxObj.btns = btns_m
        return show()
    }


    fun show(title: String, vararg btns: String): MessageBox{
        messageBoxObj.title = title
        messageBoxObj.btns = btns
        return show()
    }

    fun hide(){
        if (messageBoxObj.dialog != null){
            messageBoxObj.dialog!!.dismiss()
        }
    }



}

