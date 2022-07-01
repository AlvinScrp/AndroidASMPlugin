package com.a.plugin.methodrecord

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor

abstract class MethodRecordTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return MethodRecordClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       return (classData.className.contains("com.a.app"))
    }
}
