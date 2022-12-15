package com.pine.lib.addone.db

class TableHeader constructor(
    var name: String = "",
    var type: String = "",
    var notnull: Int = 1,
    var dflt_value: Any? = null,
    var pk: Int = 0,
    var cid: Int = 0,
){

}