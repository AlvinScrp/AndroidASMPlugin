package com.a.plugin.privacyhook

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.util.regex.Pattern

class PrivacyHookClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
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
                    var source =
                        "${PrivacyHookConfig.opCode[opcodeAndSource]} ${owner}.$name $descriptor"
                    var hookBean = PrivacyHookConfig.hooks[source]
                    var target = hookBean
                        ?.takeIf { it != null }
                        ?.takeIf {
                            hookBean?.includes?.any { Pattern.matches(it, className) } ?: false
                        }
                        ?.takeUnless {
                            hookBean?.exclues?.any { Pattern.matches(it, className) } ?: false
                        }?.let {
                            it.target
                        }

                    if (target != null) {
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            target.owner,
                            target.name,
                            target.desc,
                            false
                        )
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


}