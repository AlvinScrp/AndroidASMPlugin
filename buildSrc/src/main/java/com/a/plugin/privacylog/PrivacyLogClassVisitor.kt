package com.a.plugin.privacylog

import com.a.plugin.privacybase.PrivacyConfig
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class PrivacyLogClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM9, nextVisitor) {

    override fun visitMethod(
        callaccess: Int,
        callname: String?,
        calldescriptor: String?,
        callsignature: String?,
        callexceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(callaccess, callname, calldescriptor, callsignature, callexceptions)
        val newMethodVisitor =
            object : AdviceAdapter(Opcodes.ASM9, methodVisitor, callaccess, callname, calldescriptor) {

                override fun visitMethodInsn(
                    opcodeAndSource: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?,
                    isInterface: Boolean
                ) {
                    // 方法开始
                    if (needAddLog(opcodeAndSource, owner, name, descriptor)) {
                        println("--------------------------------------------------")
                        println("${owner}.${name} ${descriptor}")
                        println("call by: ${className}.${callname} ${calldescriptor}")
                        var log = PrivacyConfig.logMethodNode
                        mv.visitLdcInsn("${owner}.${name}")
                        mv.visitMethodInsn(
                            INVOKESTATIC, log.owner, log.name, log.descriptor, false
                        )
                    }
                    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                }

            }
        return newMethodVisitor
    }

    private fun needAddLog(
        opcodeAndSource: Int,
        owner: String?,
        name: String?,
        descriptor: String?
    ): Boolean {
        var hitClass = PrivacyConfig.checkClasses.contains(owner)

        if (!hitClass) {
            return false
        }
//        println("hitClass $owner $hitClass")
        var source = "${PrivacyConfig.opCode[opcodeAndSource]} ${owner}.$name $descriptor"
        var hitMethod = PrivacyConfig.methodSet.contains(source)
//        println("hitMethod $source $hitMethod")
        return hitMethod
    }
}