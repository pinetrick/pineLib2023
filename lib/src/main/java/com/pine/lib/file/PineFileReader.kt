package com.pine.lib.file

import java.io.File

object PineFileReader {
    fun read(filePath: String): String {
        val file = File(filePath)
        return file.readText()
    }
}