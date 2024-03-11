package com.a.plugin.test

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodNode
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

class MethodReferenceAdapter(classVisitor: ClassVisitor?) : ClassNode(Opcodes.ASM9) {
    private val counter = AtomicInteger(0)
    private val syntheticMethodList: MutableList<MethodNode> = ArrayList()

    init {
        cv = classVisitor
    }

    override fun visitEnd() {
        super.visitEnd()
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
                    val tmpNode = node
                    //形如：(Ljava/util/Date;)Ljava/util/function/Consumer;   可以从 desc 中获取函数式接口，以及动态参数的内容。
                    //如果没有参数那么描述符的参数部分应该是空。
                    val desc = tmpNode.desc
                    val descType = Type.getType(desc)
                    val samBaseType = descType.returnType
                    //sam 接口名
                    val samBase = samBaseType.descriptor
                    //sam 方法名
                    val samMethodName = tmpNode.name
                    val bsmArgs = tmpNode.bsmArgs
                    //sam 方法描述符
                    val samMethodType = bsmArgs[0] as Type
                    //sam 实现方法实际参数描述符
                    val implMethodType = bsmArgs[2] as Type
                    //sam name + desc，可以用来辨别是否是需要 Hook 的 lambda 表达式
                    val bsmMethodNameAndDescriptor = samMethodName + samMethodType.descriptor
                    //中间方法的名称
                    val middleMethodName = "lambda$" + samMethodName + "\$sa" + counter.incrementAndGet()
                    //中间方法的描述符
                    var middleMethodDesc = ""
                    val descArgTypes = descType.argumentTypes
                    if (descArgTypes.size == 0) {
                        middleMethodDesc = implMethodType.descriptor
                    } else {
                        middleMethodDesc = "("
                        for (tmpType in descArgTypes) {
                            middleMethodDesc += tmpType.descriptor
                        }
                        middleMethodDesc += implMethodType.descriptor.replace("(", "")
                    }
                    //INDY 原本的 handle，需要将此 handle 替换成新的 handle
                    val oldHandle = bsmArgs[1] as Handle
                    val newHandle = Handle(Opcodes.H_INVOKESTATIC, name, middleMethodName, middleMethodDesc, false)
                    val newDynamicNode = InvokeDynamicInsnNode(
                        tmpNode.name,
                        tmpNode.desc,
                        tmpNode.bsm,
                        samMethodType,
                        newHandle,
                        implMethodType
                    )
                    iterator.remove()
                    iterator.add(newDynamicNode)
                    generateMiddleMethod(oldHandle, middleMethodName, middleMethodDesc)
                }
            }
        })
        methods.addAll(syntheticMethodList)
        accept(cv)
    }

    private fun generateMiddleMethod(oldHandle: Handle, middleMethodName: String, middleMethodDesc: String) {
        //开始对生成的方法中插入或者调用相应的代码
        val methodNode = MethodNode(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC /*| Opcodes.ACC_SYNTHETIC*/,
            middleMethodName, middleMethodDesc, null, null
        )
        methodNode.visitCode()



        // 此块 tag 具体可以参考: https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokedynamic
        var accResult = oldHandle.tag
        when (accResult) {
            Opcodes.H_INVOKEINTERFACE -> accResult = Opcodes.INVOKEINTERFACE
            Opcodes.H_INVOKESPECIAL ->                 //private, this, super 等会调用
                accResult = Opcodes.INVOKESPECIAL

            Opcodes.H_NEWINVOKESPECIAL -> {
                //constructors
                accResult = Opcodes.INVOKESPECIAL
                methodNode.visitTypeInsn(Opcodes.NEW, oldHandle.owner)
                methodNode.visitInsn(Opcodes.DUP)
            }

            Opcodes.H_INVOKESTATIC -> accResult = Opcodes.INVOKESTATIC
            Opcodes.H_INVOKEVIRTUAL -> accResult = Opcodes.INVOKEVIRTUAL
        }
        val middleMethodType = Type.getType(middleMethodDesc)
        val argumentsType = middleMethodType.argumentTypes
        if (argumentsType.size > 0) {
            var loadIndex = 0
            for (tmpType in argumentsType) {
                val opcode = tmpType.getOpcode(Opcodes.ILOAD)
                methodNode.visitVarInsn(opcode, loadIndex)
                loadIndex += tmpType.size
            }
        }
        methodNode.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "com/a/privacy_sample/AutoTrackUtil",
            "autoTrackClickString",
            "(Ljava/lang/String;)V",
            false
        )
        if (argumentsType.size > 0) {
            var loadIndex = 0
            for (tmpType in argumentsType) {
                val opcode = tmpType.getOpcode(Opcodes.ILOAD)
                methodNode.visitVarInsn(opcode, loadIndex)
                loadIndex += tmpType.size
            }
        }
        methodNode.visitMethodInsn(accResult, oldHandle.owner, oldHandle.name, oldHandle.desc, false)
        val returnType = middleMethodType.returnType
        val returnOpcodes = returnType.getOpcode(Opcodes.IRETURN)
        methodNode.visitInsn(returnOpcodes)
        methodNode.visitEnd()
        syntheticMethodList.add(methodNode)
    }
}
