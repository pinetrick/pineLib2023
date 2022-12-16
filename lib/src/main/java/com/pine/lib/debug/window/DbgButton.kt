package com.pine.lib.debug.window


import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.pine.lib.R
import com.pine.lib.addone.MyTimer
import com.pine.lib.app.a
import com.pine.lib.app.c
import com.pine.lib.view.message_box.MessageBox


class DbgButton : View.OnTouchListener, View.OnClickListener {

    val availableBtns: ArrayList<IDebugBtns> = ArrayList()

    private var isAdded = false // 是否已增加悬浮窗
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var wm: WindowManager
    private lateinit var baseView: View

    var isMsgBoxShowing = false

    init {
        createFloatView()
    }

    fun createFloatView() {
        val mInflater = c().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        baseView = mInflater.inflate(
            R.layout.debug_window_button,
            null
        )

        wm = c().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        params = WindowManager.LayoutParams()
        val debugBtn = baseView.findViewById<Button>(R.id.btn_debug)

        // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        /*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */params.format = PixelFormat.RGBA_8888 // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        /*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

        // 设置悬浮窗的长得宽
        params.width = 100
        params.height = 100
        val h: Int = 44
        params.x = 0
        params.y = -h
        // 设置悬浮窗的Touch监听
        debugBtn.setOnTouchListener(this)
        debugBtn.setOnClickListener(this)
        try {
            wm.addView(baseView, params)
            isAdded = true
        } catch (e: Exception) {
            onCreateWindowFailed()
        }
    }

    fun onCreateWindowFailed() {
        if (isMsgBoxShowing) return

        MessageBox().setOnBtnClickListener {
            when (it) {
                3 -> {
                    a().startActivityForResult(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + c().packageName)
                        ), 0
                    )

                    MyTimer().setInterval(1000).setOnTimerListener {
                        createFloatView()
                        if (isAdded) {
                            isMsgBoxShowing = false
                            it.stop()
                        }
                    }.start(10)
                }
                else -> isMsgBoxShowing = false
            }

        }.show("调试器无法运行，请检查是否有创建悬浮窗权限！", "不再提醒", "忽略", "去开启")
        isMsgBoxShowing = true
    }

    companion object {
        private val dbgButton = DbgButton()

        fun i(): DbgButton {
            return dbgButton
        }
    }

    var lastX = 0
    var lastY: Int = 0
    var paramX = 0
    var paramY: Int = 0


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                paramX = params.x
                paramY = params.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx: Int = event.rawX.toInt() - lastX
                val dy: Int = event.rawY.toInt() - lastY
                params.x = paramX + dx
                params.y = paramY + dy
                // 更新悬浮窗位置
                wm.updateViewLayout(baseView, params)
            }
        }
        return false
    }

    override fun onClick(p0: View?) {
        DebugWindow.i().show()
    }

}