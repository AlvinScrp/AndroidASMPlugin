package com.a.plugin.privacylog

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode

class PrivacyLogPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("privacyLog", PrivacyLogExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            println("${project.name} ${variant.name}")

            resolvePrivacyLogExtension(project)

            if (variant is ApplicationVariant) {
                variant.instrumentation.transformClassesWith(
                    PrivacyLogTransform::class.java,
                    InstrumentationScope.ALL
                ) {}
                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                )
            }
        }


    }

    private fun resolvePrivacyLogExtension(project: Project) {
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
    }

    private fun parseLogMethod(sourceStr: String?): MethodInsnNode? {
        try {
            sourceStr?.let { str ->
                var ss = str.split(" ")
                var sss = ss[0].split(".")
                return MethodInsnNode(Opcodes.INVOKESTATIC,sss[0], sss[1], ss[1])
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null


    }

}