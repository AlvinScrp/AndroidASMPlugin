package com.a.plugin.autotrack

import com.a.plugin.common.InstrumentationParametersImpl
import com.a.plugin.common.output
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import org.objectweb.asm.Type


abstract class AutoTrackClickAsmTransform : AsmClassVisitorFactory<InstrumentationParametersImpl> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor)
        : ClassVisitor {
        return AutoTrackClickClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return AutoTrackContext.isClickInstrumentable(classData)
    }
}

class AutoTrackClickClassVisitor(classVisitor: ClassVisitor?) : ClassNode(Opcodes.ASM9) {

    init {
        cv = classVisitor
    }

    companion object {
        val ClickInterface: String = "android/view/View\$OnClickListener"
        val ViewTypeDescriptor = "Landroid/view/View;"
    }

    val clickSource = MethodInsnNode(
        Opcodes.INVOKEVIRTUAL,
        "L${ClickInterface}",
        "onClick",
        "(${ViewTypeDescriptor})V"
    )

    override fun visitEnd() {
        super.visitEnd()
        filterNormalMethodNodes().forEach { hookMethod(it) }
        if (AutoTrackContext.config?.hookClickLambda == true) {
            filterLambdaImplMethodNodes().forEach { hookMethod(it) }
        }
        accept(cv)
    }


    private fun filterNormalMethodNodes(): MutableSet<MethodNode> {
        val methodList = mutableSetOf<MethodNode>()

        for (methodNode in methods) {
            val clickSource = clickSource
            if (interfaces.contains(ClickInterface)
                && clickSource.name == methodNode.name
                && clickSource.desc == methodNode.desc
            ) {
                output(AutoTrackContext.config) {
                    "---NormalMethodNode--> ${methodNode.name}${methodNode.desc}   [className:${name}]"
                }
                methodList.add(methodNode)
            }
        }
        return methodList
    }

    private fun filterLambdaImplMethodNodes(): MutableSet<MethodNode> {
        val shouldHookMethodList = mutableSetOf<MethodNode>()
        for (methodNode in methods) {
            val clickSource = clickSource
            val instructions = methodNode.instructions.filterIsInstance<InvokeDynamicInsnNode>()
            for (insn in instructions) {

                //onClick
                val samMethodName = insn.name
                if (clickSource.name != samMethodName) {
                    continue
                }
                //Landroid/view/View$OnClickListener
                val samOwnerName = Type.getType(insn.desc).returnType.descriptor.removeSuffix(";")
                if (clickSource.owner != samOwnerName) {
                    continue
                }
                //(Landroid/view/View;)V
                val samMethodDescriptor = (insn.bsmArgs[0] as? Type)?.descriptor
                if (clickSource.desc != samMethodDescriptor) {
                    continue
                }
                //bsmArgs[0]: samMethodType (Landroid/view/View;)V
                //bsmArgs[1]  implMethod  lambda 指向的目标方法
                //bsmArgs[2]  instantiatedMethodType  (Landroid/view/View;)V
                val handle = insn.bsmArgs[1] as? Handle //implMethod  lambda 指向的目标方法
                if (handle != null) {
                    output(AutoTrackContext.config) {
                        "---InvokeDynamicInsnNode ->  $samMethodName$samMethodDescriptor  [className:${name}]"
                    }
                    //找到 lambda 指向的目标方法
                    val method = methods.find { it.name == handle.name && it.desc == handle.desc }!!
                    shouldHookMethodList.add(method)
                }
            }
        }
        return shouldHookMethodList
    }

    private fun hookMethod(methodNode: MethodNode) {
        val t = AutoTrackContext.config?.clickProbeInsn ?: return
        methodNode.instructions.takeIf { it != null && it.size() > 0 }
            ?.let { instructions ->
                val list = InsnList()
                val viewArgIndex = findViewArgIndex(methodNode)
                list.add(VarInsnNode(Opcodes.ALOAD, viewArgIndex))
                list.add(MethodInsnNode(t.opcode, t.owner, t.name, t.desc, t.itf))
                //插入instructions前
                instructions.insert(list)
            }
    }

    private fun findViewArgIndex(methodNode: MethodNode): Int {
        val isStatic = (methodNode.access and Opcodes.ACC_STATIC) != 0
        var varSlot = if (isStatic) 0 else 1
        val types = Type.getType(methodNode.desc).argumentTypes
        for (type in types) {
            if (type.descriptor == ViewTypeDescriptor) {
                return varSlot
            }
            varSlot += type.size
        }
        return -1
    }
}
