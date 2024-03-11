package com.a.plugin.common

import java.io.File
import java.io.FileOutputStream

object RecordSaveUtil {
    private var recordFilePath: String? = null
    private var inited = false
    fun initRecordFile(buildDir: String) {
        recordFilePath = "$buildDir/asm_record.txt"
        inited = false
        createFileOnce()
    }

    //保存文本文件
    fun save(txt: String) {
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