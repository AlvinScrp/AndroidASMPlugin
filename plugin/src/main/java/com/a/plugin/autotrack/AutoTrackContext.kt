package com.a.plugin.autotrack

import com.a.plugin.common.ConfigUtil
import com.a.plugin.common.DigestUtil
import com.a.plugin.common.IConfigBean
import com.a.plugin.common.RecordSaveUtil
import com.a.plugin.common.classNameSlash
import com.a.plugin.common.fromJson
import com.a.plugin.common.toMethodInsnNode
import com.a.plugin.privacylog.PrivacyLogExtension
import com.android.build.api.instrumentation.ClassData
import com.google.gson.Gson
import org.gradle.api.Project
import org.objectweb.asm.tree.MethodInsnNode
import java.io.File
import java.util.regex.Pattern

open class AutoTrackExtension {
    var enable = true

    var configFile: File? = null
}

class AutoTrackConfigBean(
    override val outputConsole: Boolean,
    override val outputBuild: Boolean,
    val resumeIncludes: List<String>?,
    val resumeProbe: String? = null,
    val clickIncludes: List<String>?,
    val hookClickLambda: Boolean? = false,
    val clickProbe: String? = null,
) : IConfigBean {
    var resumeProbeInsn: MethodInsnNode? = null
    var clickProbeInsn: MethodInsnNode? = null

}




object AutoTrackContext {
    var enable = true
    var config: AutoTrackConfigBean? = null
    var configSign: String = ""

    fun configByExtension(project: Project) {
        config = null
        val extension =
            project.extensions.getByType(AutoTrackExtension::class.java)
                ?.takeIf { it.enable } ?: throw IllegalArgumentException("Auto Track 配置失败")

        val json = ConfigUtil.fromFile(extension?.configFile)
        configSign = "${DigestUtil.md5(json)})-${extension.enable}"

        config = json.fromJson()
        println("=====autoTrackJson====${Gson().toJson(config)}")
        config?.let {
            it.clickProbeInsn = it.clickProbe?.toMethodInsnNode()
            it.resumeProbeInsn = it.resumeProbe?.toMethodInsnNode()
        }
    }

    fun isClickInstrumentable(classData: ClassData): Boolean {
        return classData.className.endsWith("autotrack.ViewListenerUtil").not()
            && config?.clickIncludes?.any { Pattern.matches(it, classData.classNameSlash()) } == true
    }

    fun isResumeInstrumentable(classData: ClassData): Boolean {
        return config?.resumeIncludes?.any { Pattern.matches(it, classData.classNameSlash()) } == true
    }



}

