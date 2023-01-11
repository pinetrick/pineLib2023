package com.pine.lib.file

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

object PineFileWriter {
    fun appendToFile(filePath: String, text: String) {
        val file = File(filePath)
        val writer = PrintWriter(FileWriter(file, true))
        writer.println(text)
        writer.close()
    }

    fun writeToFile(filePath: String, text: String) {
        val file = File(filePath)
        file.deleteOnExit()
        val writer = PrintWriter(FileWriter(file, false))
        writer.println(text)
        writer.close()
    }
}