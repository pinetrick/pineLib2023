package com.pine.lib_proj

import android.os.Bundle
import com.pine.lib.app.PineActivity


class MainActivity : PineActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan()
    }

    fun scan() {

    }
}