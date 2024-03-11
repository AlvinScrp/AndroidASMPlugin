package com.a.plugin.privacyhook

import com.a.plugin.autotrack.AutoTrackContext
import com.a.plugin.common.ConfigUtil
import com.a.plugin.common.DigestUtil
import com.a.plugin.common.IConfigBean
import com.a.plugin.common.classNameSlash
import com.a.plugin.common.fromJson
import com.a.plugin.common.toMethodInsnNode
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

object PrivacyHookContext {
    var enable: Boolean = true
    var config: PrivacyHookConfigBean? = null
    var configSign: String = ""

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

        hookMap.clear()
        val extension = project.extensions.getByType(PrivacyHookExtension::class.java)
            ?.takeIf { it.enable } ?: throw IllegalArgumentException("privacy hook 配置失败")

        val json = ConfigUtil.fromFile(extension?.configFile)
        configSign = "${DigestUtil.md5(json)})-${extension.enable}"
        config = json.fromJson()
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