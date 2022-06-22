package com.a.plugin.privacyhook

import org.objectweb.asm.tree.MethodInsnNode
import java.io.File

open class PrivacyHookExtension {
    //    var logMethod:String?=""
    var config: File? = null
}

data class MethodHookBean(
//    var source: String? = null,
//    var target: MethodInsnNode? = null,
    var owner:String?=null,
    var name:String?=null,
    var includes: MutableSet<String>? = null,
    var exclues: MutableSet<String>? = null,
)