package com.a.plugin.privacylog

open class PrivacyLogExtension {
    var logMethod:String?=""
    var ignorePackages:Array<String>? = arrayOf()
    var cacheOutputs = true

    override fun toString(): String {
        return "PrivacyLogExtension(logMethod='$logMethod', ignorePackages=${ignorePackages.contentToString()})"
    }


}