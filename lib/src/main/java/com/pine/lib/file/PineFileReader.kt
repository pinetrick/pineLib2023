package com.pine.lib.file

import java.io.File

object PineFileReader {
    fun read(filePath: String): String? {
        return try {
            val file = File(filePath)
            file.readText()
        } catch (_: Exception){
            null
        }

    }
}