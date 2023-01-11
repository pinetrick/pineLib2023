package com.pine.lib.addone.base_class_ext

import java.io.OutputStream

fun OutputStream.writeln(text: String = "") {
    this.write((text + "\r\n").toByteArray())
}