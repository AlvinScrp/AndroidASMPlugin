package com.a.plugin.droidasm

import com.a.plugin.test.opcodeString
import org.apache.commons.io.FilenameUtils
import org.junit.Test
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import kotlin.jvm.internal.Intrinsics

class DroidAsmContextTest {
    /**
    Checks a filename to see if it matches the specified wildcard matcher, always testing case-sensitive.
    The wildcard matcher uses the characters '?' and '*' to represent a single or multiple (zero or more) wildcard characters. This is the same as often found on Dos/Unix command lines. The check is case-sensitive always.
    wildcardMatch("c.txt", "*.txt")      --> true
    wildcardMatch("c.txt", "*.jpg")      --> false
    wildcardMatch("a/b/c.txt", "a/b/*")  --> true
    wildcardMatch("c.txt", "*.???")      --> true
    wildcardMatch("c.txt", "*.????")     --> false

    N.B. the sequence "*?" does not work properly at present in match strings.
    */

     */
    @Test
    fun wildcardMatchTest() {
        println(FilenameUtils.wildcardMatch("com.a.b", "com.a.b"))
        println(FilenameUtils.wildcardMatch("com.a.b", "*"))
        println(FilenameUtils.wildcardMatch("com.a.b", ".*"))
        println(FilenameUtils.wildcardMatch("com.a.b", "*.*"))
        println(FilenameUtils.wildcardMatch("com.a.b", "com.*"))


    }

    @Test
    fun stackTraceTest() {
        var s = "java.lang.Throwable: android/telephony/TelephonyManager.getSimCountryIso\n" +
            "\tat com.webuy.utils.privacy.PrivacyLog.w(PrivacyLog.kt:16)\n" +
            "\tat com.a.asm.sample.PrivacyVisitor.getSimCountryIso(PrivacyVisitor.java:66)\n" +
            "\tat com.a.asm.sample.PrivacyVisitor.visitPrivacy(PrivacyVisitor.java:39)\n" +
            "\tat com.a.asm.sample.AsmMainActivity.getPrivacyInfo(AsmMainActivity.kt:56)\n" +
            "\tat com.a.asm.sample.AsmMainActivity.setViewOnclick\$lambda-0(AsmMainActivity.kt:40)\n" +
            "\tat com.a.asm.sample.AsmMainActivity.\$r8\$lambda\$b_0ZWb2p6nncxf8elNxVrAH-JAw(Unknown Source:0)\n" +
            "\tat com.a.asm.sample.AsmMainActivity\$\$ExternalSyntheticLambda0.onClick(Unknown Source:2)\n" +
            "\tat android.view.View.performClick(View.java:6619)\n" +
            "\tat android.view.View.performClickInternal(View.java:6596)\n" +
            "\tat android.view.View.access\$3100(View.java:785)\n" +
            "\tat android.view.View\$PerformClick.run(View.java:25932)\n" +
            "\tat android.os.Handler.handleCallback(Handler.java:873)\n" +
            "\tat android.os.Handler.dispatchMessage(Handler.java:99)\n" +
            "\tat android.os.Looper.loop(Looper.java:201)\n" +
            "\tat android.app.ActivityThread.main(ActivityThread.java:6861)\n" +
            "\tat java.lang.reflect.Method.invoke(Native Method)\n" +
            "\tat com.android.internal.os.RuntimeInit\$MethodAndArgsCaller.run(RuntimeInit.java:547)\n" +
            "\tat com.android.internal.os.ZygoteInit.main(ZygoteInit.java:873)\n"
        s.split("\n\t").filterIndexed { index, _ -> index != 1 }.take(7).joinToString(separator = "\n\t")
            .let { println(it) }
    }


    @Test
    fun splitTest() {
        "com/webuy/utils/privacy/PrivacyLog w (Ljava/lang/String;)V".split(" ").let {
            println(it[0])
            println(it[1])
            println(it[2])
        }
    }

    @Test
    fun typeToOpcodeTest() {
        println("--------------->")
        var argTypeStr = "Ljava/lang/String;" +
            "Landroid/view/View\$OnClickListener;" +
            "I[Ljava/lang/Integer;JLjava/util/List;Ljava/util/List;"
        var methodType = Type.getType("(${argTypeStr})I")

        methodType.argumentTypes.forEach {
            val isArray = it.sort == Type.ARRAY
//            it.size
            val testOpcode = if (isArray) Opcodes.IALOAD else Opcodes.ILOAD
            println(" ${opcodeString(it.getOpcode(testOpcode))} , ${it.descriptor}")
        }
        println("--------------->")

        val types: Array<Type> = Type.getArgumentTypes("(Ljava/lang/Object;Landroid/view/View;)V")
        types.forEach {
            println(it.internalName)
        }
    }

    @Test
    fun emptyRun() {
        var s: String? = "kk"
        s?.let { println("${s} isNotNull") } ?: runCatching { println("s is null") }
    }
}











