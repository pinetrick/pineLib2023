package com.pine.lib.view.db

import android.app.Activity
import android.os.Bundle
import com.pine.lib.R
import com.pine.lib.app.PineActivity

class DatabaseActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        PineActivity.onCreate(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_activity)
    }


    override fun onResume() {
        PineActivity.onResume(this)
        super.onResume()
    }
}