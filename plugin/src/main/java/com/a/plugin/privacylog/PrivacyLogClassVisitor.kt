package com.a.plugin.privacylog

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
                    // 方法开始
                    if (isMethodInsnNeedAddLog(opcodeAndSource, owner, name, descriptor)) {
                        addLog(opcodeAndSource, owner, name, descriptor)
                    }
                    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                }

                override fun visitFieldInsn(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?
                ) {
                    if (isFieldInsnNeedAddLog(opcode, owner, name, descriptor)) {
                        addLog(opcode, owner, name, descriptor)
                    }
                    super.visitFieldInsn(opcode, owner, name, descriptor)
                }

                private fun addLog(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?
                ) {
                    val msg = "------\n" +
                            "${PrivacyLogConfig.opCode[opcode]} ${owner}.${name} ${descriptor}\n" +
                            "call by: ${className}.${callname} ${calldescriptor}\n"
                    println(msg)
                    PrivacyLogFileUtil.record(msg+"\n")
                    var log = PrivacyLogConfig.logMethodNode
                    mv.visitLdcInsn("${owner}.${name}")
                    mv.visitMethodInsn(INVOKESTATIC, log.owner, log.name, log.desc, false)
                }

            }
        return newMethodVisitor
    }


    private fun isMethodInsnNeedAddLog(
        opcodeAndSource: Int, owner: String?, name: String?, descriptor: String?
    ): Boolean {
        var hitClass = PrivacyLogConfig.checkClasses.contains(owner)

        if (!hitClass) {
            return false
        }
//        println("hitClass $owner $hitClass")
        var source = "${PrivacyLogConfig.opCode[opcodeAndSource]} ${owner}.$name $descriptor"
        var hitMethod = PrivacyLogConfig.methodSet.contains(source)
//        println("hitMethod $source $hitMethod")
        return hitMethod
    }

    private fun isFieldInsnNeedAddLog(
        opcodeAndSource: Int, owner: String?, name: String?, descriptor: String?
    ): Boolean {
        var hitClass = PrivacyLogConfig.checkClasses.contains(owner)

        if (!hitClass) {
            return false
        }
//        println("hitClass $owner $hitClass")
        var source = "${PrivacyLogConfig.opCode[opcodeAndSource]} ${owner}.$name : $descriptor"
        var hit = PrivacyLogConfig.fieldSet.contains(source)
//        println("hitMethod $source $hitMethod")
        return hit
    }
}