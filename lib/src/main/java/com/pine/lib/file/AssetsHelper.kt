package com.pine.lib.file

import com.pine.lib.app.c
import java.io.BufferedReader
import java.io.InputStreamReader

object AssetsHelper {
    fun read(filename: String): String {
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
}