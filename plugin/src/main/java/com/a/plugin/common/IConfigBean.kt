package com.a.plugin.common

import com.a.plugin.autotrack.AutoTrackContext

interface IConfigBean {
    val outputConsole: Boolean
    val outputBuild: Boolean
}

fun output(config: IConfigBean?, messageFunc: () -> String) {
    config?.let {
        if (it.outputConsole || it.outputBuild) {
            val message = messageFunc()
            if (it.outputBuild) RecordSaveUtil.save(message)
            if (it.outputConsole) println(message)
        }
    }
}