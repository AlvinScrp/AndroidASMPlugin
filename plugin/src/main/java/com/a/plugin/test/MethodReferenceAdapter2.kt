package com.a.plugin.test

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

class MethodReferenceAdapter2(classVisitor: ClassVisitor?) : ClassNode(Opcodes.ASM9) {
    private val counter = AtomicInteger(0)
    private val syntheticMethodList: MutableList<MethodNode> = ArrayList()

    init {
        cv = classVisitor
    }

    override fun visitEnd() {
        super.visitEnd()
        val shouldHookMethodList = mutableSetOf<MethodNode>()
        methods.forEach(Consumer { methodNode: MethodNode ->
            val iterator = methodNode.instructions.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                if (node is InvokeDynamicInsnNode) {
                    val idNode = node
                    println(idNode.name + " & " + idNode.desc + " & " + idNode.bsm)
                    if (idNode.name != "def") {
                        continue
                    }
                    val handle = idNode.bsmArgs[1] as? Handle
                    if (handle != null) {
                        //找到 lambda 指向的目标方法
                        val nameWithDesc = handle.name + handle.desc
                        val method = methods.find { it.name == handle.name && it.desc==handle.desc }!!
                        shouldHookMethodList.add(method)
                    }
                }
            }
        })
        shouldHookMethodList.forEach {
            hookMethod(modeNode = it)
        }
        accept(cv)
    }

    private fun hookMethod(modeNode: MethodNode) {
        val instructions = modeNode.instructions
        if (instructions != null && instructions.size() > 0) {
            val list = InsnList()
            list.add(
                VarInsnNode(Opcodes.ALOAD, 0)
            )
            list.add(
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "com/a/privacy_sample/AutoTrackUtil",
                    "autoTrackClickString",
                    "(Ljava/lang/String;)V",
                    false
                )
            )
            instructions.insert(list)
        }
    }


}
