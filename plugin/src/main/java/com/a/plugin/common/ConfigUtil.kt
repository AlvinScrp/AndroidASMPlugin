package com.a.plugin.common

import java.io.File

object ConfigUtil {
    fun  fromFile(configFile: File?): String {
        val file = configFile
            ?.takeIf { it.isFile && it.exists() && it.length() > 0 }
            ?: run { throw IllegalArgumentException("配置文件异常") }
        return file.bufferedReader().use { it.readText() }
    }
}

