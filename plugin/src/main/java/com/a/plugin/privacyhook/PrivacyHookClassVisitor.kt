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
//                    println(source)
                    var hook = PrivacyHookConfig.hooks[source]
                        ?.takeIf { it != null }
                        ?.takeIf { it ->
                            it.includes?.any { include -> Pattern.matches(include, className) }
                                ?: false
                        }
                        ?.takeUnless {
                            it.exclues?.any { exclude -> Pattern.matches(exclude, className) }
                                ?: false
                        }?.takeUnless {
                            it?.owner.isNullOrEmpty() || it?.name.isNullOrEmpty()
                        }

                    if (hook == null) {
                        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                        return
                    }
                    var desc = descriptor
                    if (opcodeAndSource == Opcodes.INVOKEVIRTUAL
                        && descriptor.isNullOrEmpty().not()
                    ) {
                        desc = "(L${owner};${desc!!.substring(1)}"
                    }
                    println("${hook.owner}.${hook.name} $desc")
                    mv.visitMethodInsn(INVOKESTATIC, hook.owner, hook.name, desc, false)
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