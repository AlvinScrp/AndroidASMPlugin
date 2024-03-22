package com.a.plugin.privacylog

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

class PrivacyLogPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("privacyLog", PrivacyLogExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        val variants = mutableSetOf<ApplicationVariant>()
        androidComponents.onVariants { variant ->
//            println("${project.name} ${variant.name}")
            if (variant is ApplicationVariant) {
                PrivacyLogContext.configByExtension(project)
                if (PrivacyLogContext.enable) {
                    variants.add(variant)
                    variant.instrumentation.transformClassesWith(
                        PrivacyLogCVFactory::class.java,
                        InstrumentationScope.ALL
                    ) {
                        it.configSign.set(PrivacyLogContext.configSign)
                    }
                    variant.instrumentation.setAsmFramesComputationMode(
                        FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                    )
                    //TODO 了解下这个instrumentation.excludes
//                    PrivacyLogContext.ignoreClasses.forEach {
//                        variant. instrumentation.excludes.add(it)
//                    }

                }
            }
        }

//        project.afterEvaluate {
//            println("afterEvaluate")
//            variants.forEach { variant ->
//                val extension = project.extensions.getByType(PrivacyLogExtension::class.java)
//                val taskName = "transform${firstUpperCase(variant.name)}ClassesWithAsm"
//                val task = project.tasks.findByName(taskName)
//                val outputs = task?.outputs
////              println("${taskName}:${task}  / outputs:${outputs} / cacheOutputs:${extension.cacheOutputs}")
//                outputs?.cacheIf { extension.cacheOutputs }
//                outputs?.upToDateWhen { extension.cacheOutputs }
//
//            }
//        }

    }


    private fun firstUpperCase(temp: String): String? {
        return temp.substring(0, 1).toUpperCase(Locale.getDefault()) + temp.substring(1)
    }

}