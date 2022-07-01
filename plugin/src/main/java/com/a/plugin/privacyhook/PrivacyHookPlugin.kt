package com.a.plugin.privacyhook

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode

class PrivacyHookPlugin : Plugin<Project> {
    override fun apply(project: Project) {
//        project.extensions.create("privacyHook", PrivacyHookExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            println("${project.name} ${variant.name}")

            resolvePrivacyHookExtension(project)

            if (variant is ApplicationVariant) {
                variant.instrumentation.transformClassesWith(
                    PrivacyHookTransform::class.java,
                    InstrumentationScope.ALL
                ) {}
                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                )
            }
        }


    }

    private fun resolvePrivacyHookExtension(project: Project) {
//        val privacyHookExtension = project.extensions.getByType(PrivacyHookExtension::class.java)

//        privacyHookExtension?.let { ext ->
//            println(ext)
//            parseHookMethod(ext.logMethod)?.let {
//                PrivacyHookConfig.logMethodNode = it
//            }
//            ext.ignorePackages?.forEach {
//                println("ignorePackages:${it}")
//                PrivacyHookConfig.ignorePackages.add(it)
//            }
//        }
    }

    private fun parseHookMethod(sourceStr: String?): MethodInsnNode? {
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