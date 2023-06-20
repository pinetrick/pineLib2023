package com.pine.lib_proj

import android.os.Bundle
import com.pine.lib.addone.db.TableHeader
import com.pine.lib.app.PineActivity
import com.pine.lib.app.model


class MainActivity : PineActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbExample()
    }

    fun dbExample() {
        model("test").create {
            arrayListOf(
                TableHeader("id", "Integer", pk = 1),
                TableHeader("name", "String"),
                TableHeader("age", "Integer"),
            )
        }
        val rec = model("test").newRecord()
        rec["name"] = "Pine"
        rec["age"] = 12
        rec.save()


    }
}