package com.a.plugin.autotrack

import com.a.plugin.common.InstrumentationParametersImpl
import com.a.plugin.common.output
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode


abstract class AutoTrackResumeAsmTransform : AsmClassVisitorFactory<InstrumentationParametersImpl> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor)
        : ClassVisitor {
        return AutoTrackResumeClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return AutoTrackContext.isResumeInstrumentable(classData)
    }
}

class AutoTrackResumeClassVisitor(classVisitor: ClassVisitor?) : ClassNode(Opcodes.ASM9) {

    init {
        cv = classVisitor
    }

    companion object {
        private const val ResumeMethodName: String = "onResume"
        private const val ResumeMethodDescriptor = "()V"
    }

    override fun visitEnd() {
        super.visitEnd()
        filterResumeMethodNodes().forEach { insertResumeTrack(it) }
        accept(cv)
    }

    private fun filterResumeMethodNodes(): MutableSet<MethodNode> {
        val methodList = mutableSetOf<MethodNode>()

        for (methodNode in methods) {
            if (methodNode.name == ResumeMethodName && methodNode.desc == ResumeMethodDescriptor) {
                output(AutoTrackContext.config) {
                    "---FragmentMethodNode--> ${methodNode.name}${methodNode.desc} [className:${name}]"
                }
                methodList.add(methodNode)
            }
        }
        return methodList
    }

    private fun insertResumeTrack(methodNode: MethodNode) {
        val t = AutoTrackContext.config?.resumeProbeInsn ?: return
        methodNode.instructions.takeIf { it != null && it.size() > 0 }
            ?.let { instructions ->
                val list = InsnList()
                //load 当前fragment对象
                list.add(VarInsnNode(Opcodes.ALOAD, 0))
                list.add(MethodInsnNode(t.opcode, t.owner, t.name, t.desc, t.itf))
                //插入instructions前
                instructions.insert(list)
            }
    }
}
