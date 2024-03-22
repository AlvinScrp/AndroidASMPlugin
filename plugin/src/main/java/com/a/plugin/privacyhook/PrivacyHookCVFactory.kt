package com.a.plugin.privacyhook

import com.a.plugin.common.InstrumentationParametersImpl
import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.util.regex.Pattern


abstract class PrivacyHookCVFactory : AsmClassVisitorFactory<InstrumentationParametersImpl> {

    override fun createClassVisitor(classContext: ClassContext, next: ClassVisitor): ClassVisitor {
        return PrivacyHookClassVisitor(next)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return PrivacyHookContext.isInstrumentable(classData)
    }
}

class PrivacyHookClassVisitor(nextVisitor: ClassVisitor) :
    ClassVisitor(Opcodes.ASM9, nextVisitor) {

    private val context = PrivacyHookContext

    private var classNameSplash: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        classNameSplash = name
        super.visit(version, access, name, signature, superName, interfaces)
    }


    override fun visitMethod(
        callaccess: Int,
        callname: String?,
        calldescriptor: String?,
        callsignature: String?,
        callexceptions: Array<out String>?
    ): MethodVisitor {

        val methodVisitor =
            super.visitMethod(callaccess, callname, calldescriptor, callsignature, callexceptions)
        val newMethodVisitor =
            object :
                AdviceAdapter(Opcodes.ASM9, methodVisitor, callaccess, callname, calldescriptor) {

                override fun visitMethodInsn(
                    opcodeAndSource: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?,
                    isInterface: Boolean
                ) {

                    var source = "$owner $name $descriptor"
//                    println(source)
                    var t = context.hookMap[source]
                        ?.takeIf { it.filterClassName(classNameSplash) }
                        ?.targetInsn
                    if (t != null) {
                        context.output() {
                            "------privacy_hook--\n" +
                                "$owner $name ${descriptor}\n" +
                                "call by: $classNameSplash $callname ${calldescriptor}\n"
                        }
                        mv.visitMethodInsn(INVOKESTATIC, t.owner, t.name, t.desc, false)
                    } else {
                        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                    }

                }

                override fun visitFieldInsn(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?
                ) {

                    super.visitFieldInsn(opcode, owner, name, descriptor)
                }


            }
        return newMethodVisitor
    }

    fun HookBean.filterClassName(className: String?): Boolean {
        className ?: return false
        val isInclude = includes.orEmpty().let {
            it.isEmpty() || it.any { v -> Pattern.matches(v, className) }
        }
        val isNotExclude = excludes.orEmpty().let {
            it.isEmpty() || it.all { v -> !Pattern.matches(v, className) }
        }
        return isInclude && isNotExclude

    }


}
