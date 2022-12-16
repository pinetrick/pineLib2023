package com.pine.lib.debug.window

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.GridView
import com.pine.lib.R
import com.pine.lib.addone.DataCleanManager
import com.pine.lib.app.a
import com.pine.lib.app.c
import com.pine.lib.app.intent
import com.pine.lib.debug.e
import com.pine.lib.view.db.db_choose.DbChooseActivity
import com.pine.lib.view.message_box.MessageBox
import kotlin.system.exitProcess


class DebugWindow : OnItemClickListener {
    private var gridView: GridView? = null
    private var dialog: DebugDialog? = null
    private var retBtn: Button? = null
    private var copyCrash: Button? = null
    private var goSetting: Button? = null
    private var checkDb: Button? = null
    private var isShowing: Boolean = false


    // 清理前端窗口
    fun dismiss() {
        isShowing = false
        if (dialog != null) {
            dialog!!.cancel()
            dialog = null
        }
    }

    /**
     * 通过这里显示提示框
     *
     * @param message
     * @param buttons
     */
    fun show() {
        if (a() == null) {
            e("消息框无法显示 - 上下文失效")
            return
        }
        if (!isShowing) {
            dialog = DebugDialog(a(), R.style.dialog)
            dialog!!.setCanceledOnTouchOutside(true) // 设置点击Dialog外部任意区域关闭Dialog
            dialog!!.show()
            dialog!!.setOnCancelListener {
                isShowing = false
            }
            isShowing = true

            retBtn = dialog!!.findViewById(R.id.debug_runtime_ret_btn) as Button
            goSetting = dialog!!
                .findViewById(R.id.debug_runtime_setting_btn) as Button
            copyCrash = dialog!!.findViewById(R.id.debug_runtime_copy_btn) as Button
            checkDb = dialog!!.findViewById(R.id.debug_runtime_db_btn) as Button
            gridView = dialog!!
                .findViewById(R.id.debug_runtime_func_list)
            gridView!!.adapter = DebugWindowAdapter()
            gridView!!.onItemClickListener = this

            retBtn!!.setOnClickListener {
                dismiss()
            }

            checkDb!!.setOnClickListener {
                intent(DbChooseActivity::class)
                dismiss()
            }

            goSetting!!.setOnClickListener {
                MessageBox().setOnBtnClickListener { id ->
                    if (id == 1) {
                        DataCleanManager.cleanInternalCache(c())
                        DataCleanManager.cleanDatabases(c())
                        DataCleanManager.cleanSharedPreference(c())
                        DataCleanManager.cleanFiles(c())
                        DataCleanManager.cleanExternalCache(c())
                        exitProcess(0)
                    } else if (id == 2) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_DELETE
                        intent.data = Uri
                            .parse("package:" + c().packageName)
                        a().startActivity(intent)
                    } else if (id == 3) {
                        exitProcess(0)
                    }
                }.show("应用将退出！", "清除数据", "卸载应用", "自杀", "取消")
            }
        }
    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    companion object {
        private var debugWindow: DebugWindow? = null

        fun i(): DebugWindow {
            if (debugWindow == null) {
                debugWindow = DebugWindow()
            }
            return debugWindow!!
        }

    }
}