package com.a.plugin.privacyhook

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor

abstract class PrivacyHookTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {


    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return PrivacyHookClassVisitor(nextClassVisitor, classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        var ignore = PrivacyHookConfig.ignorePackages.any { classData.className.startsWith(it) }
        var instrumentable = !ignore
        return instrumentable
    }
}
