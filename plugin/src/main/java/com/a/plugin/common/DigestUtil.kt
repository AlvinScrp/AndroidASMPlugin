package com.a.plugin.common

import java.io.File
import java.security.MessageDigest
import java.util.Formatter


object DigestUtil {
//    fun md5(file: File?): String {
//        val text = file?.bufferedReader()?.use { it.readText() } ?: throw IllegalArgumentException("配置文件MD5失败")
//        return md5(text)
//    }

     fun md5(text: String): String {
        var sig = ""

        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.reset()
        messageDigest.update(text.toByteArray())

        val cipherData = messageDigest.digest()

        val formatter = Formatter()
        for (data in cipherData) {
            formatter.format("%02x", data)
        }

        sig = formatter.toString()
        formatter.close()


        return sig
    }
}