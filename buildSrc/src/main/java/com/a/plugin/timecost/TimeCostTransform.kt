package com.a.plugin.timecost

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor

abstract class TimeCostTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return TimeCostClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       return (classData.className.contains("com.a.app"))
    }
}
