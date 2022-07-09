package com.a.plugin.privacylog

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object PrivacyLogFileUtil {
    private var recordFilePath: String? = null
    private var inited = false
    fun initRecordFilePath(path: String) {
        recordFilePath = path
        inited = false
    }

//    //保存文本文件
//    fun record(txt: String) {
//        createFileOnce()
//        var out: BufferedWriter? = null
//        try {
//            out = BufferedWriter(OutputStreamWriter(FileOutputStream(recordFilePath)))
//            out.write(txt)
//            out.write("\r\n")
//            out.flush()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                out?.close()
//            } catch (e2: Exception) {
//                e2.printStackTrace()
//            }
//        }
//    }

    //保存文本文件
    fun record(txt: String) {
        try {
            createFileOnce()
            val fos = FileOutputStream(recordFilePath, true)
            fos.write(txt.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun createFileOnce() {
        if (!inited) {
            inited = true
            try {
                var file = File(recordFilePath)
                var parentDir = file.parentFile
                if (!parentDir.exists()) parentDir.mkdirs()
                if (file.exists()) file.delete()
                file.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}