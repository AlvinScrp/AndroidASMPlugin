package com.a.plugin.common

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface InstrumentationParametersImpl : InstrumentationParameters {
    /**
     * Input:当该注解应用于属性时，Gradle 会在该属性的值发生变化时将任务视为过时。这确保了当任务的输入发生变化时重新运行任务。
     */
    @get:Input
    val configSign: Property<String>
}
