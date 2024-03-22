package com.a.plugin.privacyhook

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class PrivacyHookPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("privacyHook", PrivacyHookExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
//            println("${project.name} ${variant.name}")
            if (variant is ApplicationVariant) {
                PrivacyHookContext.configByExtension(project)
                val context = PrivacyHookContext
                if (context.enable) {
//                    println(variant.instrumentation.toString())
                    variant.instrumentation.transformClassesWith(
                        PrivacyHookCVFactory::class.java,
                        InstrumentationScope.ALL
                    ) {
                        it.configSign.set(context.configSign)
                    }
                    variant.instrumentation.setAsmFramesComputationMode(
                        FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                    )
                }
            }
        }
    }

}