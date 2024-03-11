package com.a.plugin.test

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor


abstract class TestVisitorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        var className = classContext.currentClassData.className
//        println("createClassVisitor ReplaceOnClickClassVisitor")
//        return  MethodReferenceAdapter(nextClassVisitor)
        return  MethodReferenceAdapter2(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return !classData.className.contains("ViewListenerUtil")
    }
}

