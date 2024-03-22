package com.a.plugin.autotrack

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

const val AutoTrackExtensionName = "autoTrack"

class AutoTrackPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(AutoTrackExtensionName, AutoTrackExtension::class.java)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
//        println("${project.name} AutoTrackPlugin apply ")

        androidComponents.onVariants { variant ->
//            println("onVariants ,${project.name}, ${variant.name}")
            if (variant is ApplicationVariant) {
                AutoTrackContext.configByExtension(project)
                val context = AutoTrackContext
                if (context.enable) {
                    listOf(
                        AutoTrackClickCVFactory::class.java,
                        AutoTrackResumeCVFactory::class.java
                    ).forEach {
                        variant.instrumentation.transformClassesWith(it, InstrumentationScope.ALL) { p ->
                            p.configSign.set(context.configSign)
                        }
                    }
                    variant.instrumentation.setAsmFramesComputationMode(
                        FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                    )
                }
            }
        }
    }

}




