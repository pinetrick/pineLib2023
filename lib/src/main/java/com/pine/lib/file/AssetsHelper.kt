package com.pine.lib.file

import com.pine.lib.app.c
import java.io.BufferedReader
import java.io.InputStreamReader

object AssetsHelper {
    fun read(filename: String): String? {
        try {
            // 获取 AssetManager 对象
            val assetManager = c().assets

            // 使用 AssetManager 读取文件
            val inputStream = assetManager.open(filename)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val text = reader.use { it.readText() }

            // 关闭输入流
            inputStream.close()
            return text
        }
        catch (e: Exception) {
            return null
        }
    }

    fun readAsByteArray(filename: String): ByteArray? {
        try {
            // 获取 AssetManager 对象
            val assetManager = c().assets

            // 使用 AssetManager 读取文件
            val inputStream = assetManager.open(filename)
            val fileContent = ByteArray(inputStream.available())
            inputStream.read(fileContent)
            inputStream.close()

            return fileContent
        }
        catch (e: Exception) {
            return null
        }
    }

}