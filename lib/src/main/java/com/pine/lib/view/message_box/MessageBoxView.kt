package com.pine.lib.view.message_box

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pine.lib.R
import com.pine.lib.app.a
import com.pine.lib.app.app
import com.pine.lib.app.c
import com.pine.lib.view.toast.toast


class MessageBoxView : AlertDialog {
    var messageBoxObj: MessageBoxObj = MessageBoxObj()
    var messageBox: MessageBox? = null

    constructor(
        context: Context,
        theme: Int,
        messageBoxObj: MessageBoxObj,
        messageBox: MessageBox
    ) : super(context, theme) {
        this.messageBoxObj = messageBoxObj
        this.messageBox = messageBox
    }


    protected constructor(context: Context) : super(context) {
    }


    protected constructor(
        context: Context, cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener) {
        // TODO Auto-generated constructor stub
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //强制Dialog全屏
        val resources = c().resources
        val dm = resources.displayMetrics

        window!!.attributes.width = dm.widthPixels


        setContentView(messageBoxObj.baseLayout)

        //强制Dialogqia
        val windowManager = a().windowManager
        val display = windowManager.defaultDisplay
        var lp = window!!.attributes

        window!!.attributes = lp

        setCanceledOnTouchOutside(messageBoxObj.cancelable)// 设置点击Dialog外部任意区域关闭Dialog

        setupTitle(messageBoxObj.title)
        setButton(messageBoxObj.btns)

        setCloseBtn()
    }

    private fun setCloseBtn() {
        val string2Id: Int = a().resources
            .getIdentifier("close_btn", "id", app().packageName) //获取标识符
        var closeBtn = findViewById<ImageView>(string2Id)
        closeBtn?.setOnClickListener {
            messageBoxObj.dialog?.dismiss()
        }
    }


    fun setOneButton(key: String, value: String?, id: Int) {
        val string2Id: Int = a().resources
            .getIdentifier(key, "id", app().packageName) //获取标识符
        if (string2Id != 0) {
            setOneButton(string2Id, value, id)
        } else {
            toast("信息窗口按钮不足，检查布局")
        }
    }

    fun setOneButton(key: Int, value: String?, id: Int) {
        setOneButton(key, key, value, id)
    }

    fun setOneButton(key: Int, layout: Int, value: String?, id: Int) {
        val btn = findViewById<TextView>(key)
        val layoutx = findViewById<View>(layout)
        if ((value != null) && (value != "")) {
            layoutx.visibility = View.VISIBLE
            btn.text = value
        }

        layoutx.setOnClickListener {

            if (messageBoxObj.callback != null) {

                messageBoxObj.callback!!(id)
            }
            dismiss()
        }
    }

    fun setViewInvisable(key: String) {
        val string2Id: Int = a().resources
            .getIdentifier(key, "id", app().packageName) //获取标识符
        if (string2Id != 0) {
            val btn = findViewById<View>(string2Id)
            if (btn != null)
                btn.visibility = View.GONE
        }
    }

    fun setButton(btns: Array<out String>) {
        for (id in 1..10) {
            setViewInvisable("btn" + id)
        }

        var id = 0
        btns.forEach {
            id++
            setOneButton("btn" + id, it, id)
        }
    }

    fun setupTitle(title: String) {
        val mainMassage = this.findViewById<TextView>(R.id.mainMassage)
        if (mainMassage != null)
            mainMassage.text = title
    }

    override fun dismiss() {
        MessageBoxManager.messageBoxShowing = null
        MessageBoxManager.messageboxQueue.remove(messageBox)

        if (MessageBoxManager.messageboxQueue.count() != 0) {
            MessageBoxManager.messageboxQueue[0].show()
        }
        super.dismiss()
    }
}