package com.a.plugin.common

import com.android.build.api.instrumentation.ClassData
import com.google.gson.Gson
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode


/**
 * com/webuy/common/utils/privacy/PrivacyRecord log (Ljava/lang/String;)V
 */
fun String.toMethodInsnNode(isStatic: Boolean = true): MethodInsnNode? {
    val node = this.split(" ")?.takeIf { it.size == 3 }?.let { ss ->
        val opcode = if (isStatic) Opcodes.INVOKESTATIC else Opcodes.INVOKEVIRTUAL
        MethodInsnNode(opcode, ss[0], ss[1], ss[2], false)
    } ?: run { throw IllegalArgumentException("toMethodInsnNode $this") }
    return node
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java) ?: throw IllegalArgumentException("json 转换异常")
}

inline fun ClassData.classNameSlash(): String {
    return this.className.replace(".", "/")
}