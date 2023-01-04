package com.pine.lib.provider.opr

import com.pine.lib.app.c
import java.io.File

class Files : BaseOpr() {


    fun refreshBaseTree() {
        val r = Dir("", "", true, ArrayList(), 0)
        r.subFiles!!.add(Dir("/", "/"))
        r.subFiles!!.add(Dir("/data/data/" + c().packageName, "/data/data/" + c().packageName))

        responseData.returnObj = r
    }


    fun loadDir() {
        val dir = requestData.args["dir"]!!
        val r = Dir(name = dir, fullDir = dir, isDir = true, subFiles = ArrayList())
        val currentDir = File(dir)
        for (file in currentDir.walk()) {
            if (file.absolutePath != dir) {
                val d = Dir(
                    fullDir = file.absolutePath,
                    name = file.name,
                    isDir = file.isDirectory,
                    subFiles = null,
                    fileSize = file.length()
                )
                r.subFiles!!.add(d)
            }
        }

        responseData.returnObj = r
    }

    fun loadFile() {
        val file = requestData.args["file"]!!

        val content = File(file).readText()

        responseData.returnObj = FileContent(name = file, content = content)
    }

    fun saveFile() {
        val file = requestData.args["file"]!!

        val content = requestData.bodyArgs["content"]

        content?.let { File(file).writeText(it) }


        responseData.content = content
    }

}

data class Dir(
    val fullDir: String = "",
    val name: String = "",
    val isDir: Boolean = true,
    val subFiles: ArrayList<Dir>? = null,
    val fileSize: Long = 0,
)

data class FileContent(
    val name: String = "",
    val content: String = "",
)