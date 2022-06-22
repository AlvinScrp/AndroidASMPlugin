package com.a.plugin.privacylog

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode
import java.util.*

class PrivacyLogPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("privacyLog", PrivacyLogExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        val variants = mutableSetOf<ApplicationVariant>()



        androidComponents.onVariants { variant ->
            println("${project.name} ${variant.name}")
           resolvePrivacyLogExtension(project)
            if (variant is ApplicationVariant) {
                variants.add(variant)
                variant.instrumentation.transformClassesWith(
                    PrivacyLogTransform::class.java,
                    InstrumentationScope.ALL
                ) {}
                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                )
            }
        }

        project.afterEvaluate {
            println("afterEvaluate")
            variants.forEach { variant->
                val extension =  project.extensions.getByType(PrivacyLogExtension::class.java)
                val taskName = "transform${firstUpperCase(variant.name)}ClassesWithAsm"
                val task = project.tasks.findByName(taskName)
                val outputs =  task?.outputs
//                println("${taskName}:${task}  / outputs:${outputs} / cacheOutputs:${extension.cacheOutputs}")
                outputs?.cacheIf { extension.cacheOutputs }
                outputs?.upToDateWhen { extension.cacheOutputs }
                PrivacyLogFileUtil.initRecordFilePath("${project.buildDir.absolutePath}/privacy_log/record.txt")
            }

        }

    }


    private fun resolvePrivacyLogExtension(project: Project): PrivacyLogExtension {
        val privacyLogExtension = project.extensions.getByType(PrivacyLogExtension::class.java)

        privacyLogExtension?.let { ext ->
            println(ext)
            parseLogMethod(ext.logMethod)?.let {
                PrivacyLogConfig.logMethodNode = it
            }
            ext.ignorePackages?.forEach {
                println("ignorePackages:${it}")
                PrivacyLogConfig.ignorePackages.add(it)
            }
        }
        return privacyLogExtension
    }

    private fun parseLogMethod(sourceStr: String?): MethodInsnNode? {
        try {
            sourceStr?.let { str ->
                var ss = str.split(" ")
                var sss = ss[0].split(".")
                return MethodInsnNode(Opcodes.INVOKESTATIC, sss[0], sss[1], ss[1])
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    private fun firstUpperCase(temp: String): String? {
        return temp.substring(0, 1).toUpperCase(Locale.getDefault()) + temp.substring(1)
    }

}