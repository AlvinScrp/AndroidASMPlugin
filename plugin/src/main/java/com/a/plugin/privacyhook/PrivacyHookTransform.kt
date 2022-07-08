package com.a.plugin.privacyhook

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor
import java.util.regex.Pattern

abstract class PrivacyHookTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {


    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return PrivacyHookClassVisitor(nextClassVisitor, classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        var ignore = PrivacyHookConfig.ignorePackages.any { classData.className.startsWith(it) }
//        var target = hookBean
//            ?.takeIf { it != null }
//            ?.takeIf {
//                hookBean.includes?.any { Pattern.matches(it, className) } ?: false
//            }
//            ?.takeUnless {
//                hookBean.exclues?.any { Pattern.matches(it, className) } ?: false
//            }?.let {
//                it.target
//            }

        var instrumentable = !ignore
//        instrumentable = classData.className.startsWith("com.a.privacy_sample")
        return instrumentable
    }
}
