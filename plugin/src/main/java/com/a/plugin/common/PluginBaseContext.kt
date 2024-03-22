package com.a.plugin.common


open class PluginBaseContext<T : IConfigBean> {

    var enable: Boolean = true
    var config: T? = null
    var configSign: String = ""
    var logger: BuildCacheLogger? = null

    fun output(messageFunc: () -> String) {
        config?.let {
            if (it.outputConsole || it.outputBuild) {
                val message = messageFunc()
                if (it.outputBuild) logger?.save(message)
                if (it.outputConsole) println(message)
            }
        }
    }
}

interface IConfigBean {
    val outputConsole: Boolean
    val outputBuild: Boolean
}

