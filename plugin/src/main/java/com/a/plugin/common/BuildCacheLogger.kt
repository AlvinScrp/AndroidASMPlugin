package com.a.plugin.common

import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class BuildCacheLogger(private var recordFilePath: String) {

    private val recordFile: File by lazy { createFileOnce() }

    private val lock = Any()

    fun save(txt: String) {
        synchronized(lock) {
            recordFile.appendText(txt)
            recordFile.appendText("\n")
        }
    }

    @Synchronized
    private fun createFileOnce(): File {
        var file = File(recordFilePath)
        var parentDir = file.parentFile
        if (!parentDir.exists()) parentDir.mkdirs()
        if (file.exists()) file.delete()
        file.createNewFile()
        return file
    }

}