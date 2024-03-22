package com.a.plugin.privacyhook

import com.a.plugin.common.BuildCacheLogger
import com.a.plugin.common.ConfigUtil
import com.a.plugin.common.DigestUtil
import com.a.plugin.common.IConfigBean
import com.a.plugin.common.PluginBaseContext
import com.a.plugin.common.classNameSlash
import com.a.plugin.common.fromJson
import com.a.plugin.common.toMethodInsnNode
import com.a.plugin.privacylog.PrivacyLogContext
import com.android.build.api.instrumentation.ClassData
import org.gradle.api.Project
import org.objectweb.asm.tree.MethodInsnNode
import java.io.File
import java.util.regex.Pattern

open class PrivacyHookExtension {
    var enable = true
    var configFile: File? = null

}

class PrivacyHookConfigBean(
    override val outputConsole: Boolean,
    override val outputBuild: Boolean,
    val globalExcludeClasses: MutableList<String>? = null,
    val hooks: List<HookBean>? = null
) : IConfigBean


data class HookBean(
    var source: String? = "",
    var target: String? = "",
    var includes: MutableSet<String>? = mutableSetOf(),
    var excludes: MutableSet<String>? = mutableSetOf()
) {
    var targetInsn: MethodInsnNode? = null
}

object PrivacyHookContext : PluginBaseContext<PrivacyHookConfigBean>() {

    private var globalExcludeClasses: MutableList<String> = mutableListOf()
    var hookMap: MutableMap<String, HookBean> = mutableMapOf()

    private var AlwaysExcludeClasses = mutableSetOf(
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
        logger = BuildCacheLogger("${project.buildDir}/asm_privacy_hook.txt")

        val extension = project.extensions.getByType(PrivacyHookExtension::class.java)
            ?: throw IllegalArgumentException("privacy hook 配置失败")
        enable = extension.enable
        val json = ConfigUtil.fromFile(extension?.configFile)
        configSign = "${DigestUtil.md5(json)})-${extension.enable}"

        config = json.fromJson()
        hookMap.clear()
        config?.hooks?.forEach { hook ->
            hook.source?.let { source ->
                hookMap[source] = hook.also { it.targetInsn = it.target?.toMethodInsnNode() }
            }
        }
        globalExcludeClasses.addAll(config?.globalExcludeClasses.orEmpty() + AlwaysExcludeClasses)

    }

    fun isInstrumentable(classData: ClassData): Boolean {
        return globalExcludeClasses.all { !Pattern.matches(it, classData.classNameSlash()) }
    }


}