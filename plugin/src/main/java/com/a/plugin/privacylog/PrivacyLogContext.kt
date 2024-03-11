package com.a.plugin.privacylog

import com.a.plugin.common.ConfigUtil
import com.a.plugin.common.DigestUtil
import com.a.plugin.common.IConfigBean
import com.a.plugin.common.classNameSlash
import com.a.plugin.common.fromJson
import com.a.plugin.common.toMethodInsnNode
import com.a.plugin.privacyhook.PrivacyHookConfigBean
import com.a.plugin.privacyhook.PrivacyHookContext
import com.a.plugin.privacyhook.PrivacyHookExtension
import com.android.build.api.instrumentation.ClassData
import com.google.gson.Gson
import org.gradle.api.Project
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode
import java.io.File
import java.util.regex.Pattern

open class PrivacyLogExtension {
    var enable = true
    var configFile: File? = null

}

class PrivacyLogConfigBean(
    override val outputConsole: Boolean,
    override val outputBuild: Boolean,
    var globalExcludeClasses: MutableSet<String>? = null,
    val log: String?,
    var includeMethods: MutableSet<String>?,
    var includeFields: MutableSet<String>?
) : IConfigBean {
    var logInsn: MethodInsnNode? = null
}


object PrivacyLogContext {
    var enable = true
    var extension: PrivacyLogExtension? = null
    var config: PrivacyLogConfigBean? = null
    var configSign: String = ""

    private var AlwaysExcludeClasses: MutableSet<String> = mutableSetOf(
        "kotlin/.*",
        "kotlinx/.*",
        "com/google/.*",
        "org/jetbrains/.*",
        "org/objectweb/.*",
        "org/intellij/.*",
        "androidx/.*",
        "android/.*",
        "io/flutter/.*"
    )


    fun configByExtension(project: Project) {
        extension = project.extensions.getByType(PrivacyLogExtension::class.java)
            ?.takeIf { it.enable } ?: throw IllegalArgumentException("privacy log 配置失败")

        val json = ConfigUtil.fromFile(extension?.configFile)
        configSign =
            "${DigestUtil.md5(json)})-${extension?.enable}"
        config = json.fromJson()

        config?.logInsn = config?.log?.toMethodInsnNode()
        val configGlobalExcludeClasses = config?.globalExcludeClasses.orEmpty()
        config?.globalExcludeClasses = mutableSetOf<String>().also {
            it.addAll(configGlobalExcludeClasses + AlwaysExcludeClasses)
        }

    }

    fun isInstrumentable(classData: ClassData): Boolean {
        return config?.globalExcludeClasses.orEmpty().all { !Pattern.matches(it, classData.classNameSlash()) }
    }

}