package com.a.plugin.privacylog

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor
import java.util.regex.Pattern

abstract class PrivacyLogTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return PrivacyLogClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       var ignore =  PrivacyLogConfig.ignorePackages.any { Pattern.matches(it,classData.className)}
               || PrivacyLogConfig.ignoreClasses.contains(classData.className)
//       var  instrumentable =  PrivacyConst.checkClasses.contains(classData.className)
        var instrumentable = !ignore

//        var instrumentable ="com.webuy.android_asm.PrivacyVisitor" == classData.className
//        if(instrumentable) {
//            println("instrumentable: ${classData.className} $instrumentable")
//        }
       return instrumentable
    }
}
