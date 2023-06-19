package com.pine.lib.provider.opr

import android.os.Environment
import com.pine.lib.addone.permission.PermissionList
import com.pine.lib.addone.permission.RequirePermission
import com.pine.lib.app.c
import java.io.File

class Files : BaseOpr() {


    fun refreshBaseTree() {
        val r = Dir("", "", true, ArrayList(), 0)
        r.subFiles!!.add(Dir(Environment.getExternalStorageDirectory().toString(), Environment.getExternalStorageDirectory().toString()))
        r.subFiles!!.add(Dir("/data/data/" + c().packageName, "/data/data/" + c().packageName))

        responseData.returnObj = r
    }


    fun loadDir() {
        val dir = requestData.args["dir"]!!
        if (dir.isEmpty()) return refreshBaseTree()

        val r = Dir(name = dir, fullDir = dir, isDir = true, subFiles = ArrayList())
        val currentDir = File(dir)
        for (file in currentDir.listFiles()) {
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

    fun renameFile() {
        RequirePermission.require(PermissionList.WRITE_EXTERNAL_STORAGE)

        val file = requestData.args["file"]!!
        val newName = requestData.args["newName"]!!

        val oldFile = File(file)
        val newFile = File(oldFile.parent + "/" + newName)
        oldFile.renameTo(newFile)


        responseData.content = "Saved"
    }

    fun saveFile() {
        RequirePermission.require(PermissionList.WRITE_EXTERNAL_STORAGE)

        val file = requestData.args["file"]!!

        val content = requestData.bodyArgs["content"]

        File(File(file).parent).mkdirs()
        content?.let { File(file).writeText(it) }


        responseData.content = "Saved"
    }

    fun delete() {
        val file = requestData.args["file"]!!



        File(file).deleteRecursively()


        responseData.content = "File Removed"
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