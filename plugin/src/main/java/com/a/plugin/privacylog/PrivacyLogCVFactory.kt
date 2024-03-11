package com.a.plugin.privacylog

import com.a.plugin.common.InstrumentationParametersImpl
import com.a.plugin.common.RecordSaveUtil
import com.a.plugin.common.output
import com.a.plugin.privacyhook.PrivacyHookContext
import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter


abstract class PrivacyLogTransform : AsmClassVisitorFactory<InstrumentationParametersImpl> {


    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return PrivacyLogClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return PrivacyLogContext.isInstrumentable(classData)
    }
}

class PrivacyLogClassVisitor(nextVisitor: ClassVisitor) :
    ClassVisitor(Opcodes.ASM9, nextVisitor) {

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
                    if (isMethodInsnMatched(owner, name, descriptor)) {
                        addLog(owner, name, descriptor)
                    }
                    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                }

                override fun visitFieldInsn(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?
                ) {
                    if (isFieldInsnMatched(owner, name, descriptor)) {
                        addLog(owner, name, descriptor)
                    }
                    super.visitFieldInsn(opcode, owner, name, descriptor)
                }

                private fun addLog(owner: String?, name: String?, descriptor: String?) {
                    output(PrivacyHookContext.config) {
                        "------privacy_log--\n" +
                            "$owner $name ${descriptor}\n" +
                            "call by: $classNameSplash $callname ${calldescriptor}\n"
                    }
                    PrivacyLogContext.config?.logInsn?.let { log ->
                        mv.visitLdcInsn("${owner}.${name}")
                        mv.visitMethodInsn(INVOKESTATIC, log.owner, log.name, log.desc, false)
                    }

                }

            }
        return newMethodVisitor
    }


    private fun isMethodInsnMatched(owner: String?, name: String?, descriptor: String?): Boolean {
        val space = " "
        var source = "${owner}${space}$name${space}$descriptor"
        return PrivacyLogContext.config?.includeMethods?.contains(source) == true
    }

    private fun isFieldInsnMatched(owner: String?, name: String?, descriptor: String?): Boolean {
        val space = " "
        var source = "${owner}${space}$name${space}$descriptor"
        return PrivacyLogContext.config?.includeFields?.contains(source) == true
    }
}
