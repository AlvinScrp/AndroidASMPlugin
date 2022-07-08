package com.a.plugin.privacyhook

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode

/**
 * 参考
 * https://github.com/yanerchuang/PrivacyPolicyComplianceCheck/blob/master/app/src/main/java/com/ywj/xposeddemo/MainActivity.java
 * TelephonyManager信息获取：https://www.cnblogs.com/weixing/p/3253479.html
 */
object PrivacyHookConfig {

    var opCode = mutableMapOf(
        Opcodes.INVOKESTATIC to "INVOKESTATIC",
        Opcodes.INVOKEVIRTUAL to "INVOKEVIRTUAL",
        Opcodes.GETSTATIC to "GETSTATIC"

    )


    var ignorePackages: MutableSet<String> =
        mutableSetOf(
            "kotlin",
            "kotlinx",
            "com.google.",
            "org.jetbrains",
            "org.objectweb",
            "org.intellij",
            "androidx",
            "android.",
            "io.flutter",
            "com.ta.a.d.e",
            "com.ta.utdid2.device.c",
            "com.idlefish"
        )


    var hooks: MutableMap<String, MethodHookBean> = mutableMapOf()

    //不可变设备信息 IMSI、IMEI，MEID ,SimSerialNumber,设备手机号，MAC地址
    var getSubscriberId =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSubscriberId ()Ljava/lang/String;" //IMSI
    var getDeviceId =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getDeviceId ()Ljava/lang/String;" //IMEI
    var getImei =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getImei ()Ljava/lang/String;" //IMEI
    var getNai =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNai ()Ljava/lang/String;" //IMEI
   var getSimCountryIso =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimCountryIso ()Ljava/lang/String;" //获取ISO国家码，相当于提供SIM卡的国家码

    //软件列表
    var getRunningAppProcesses =
        "INVOKEVIRTUAL android/app/ActivityManager.getRunningAppProcesses ()Ljava/util/List;" //运行进程

    //Android ID 还需要检测方法参数为 "android_id"
    var Secure_getString =
        "INVOKESTATIC android/provider/Settings\$Secure.getString (Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;" //Android Id?
    var System_getString =
        "INVOKESTATIC android/provider/Settings\$System.getString (Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;" //Android Id?

    var getSharedPreferences =
        "INVOKEVIRTUAL android/content/Context.getSharedPreferences (Ljava/lang/String;I)Landroid/content/SharedPreferences;"

    init {
        var hooksTargetClassName = "cn/com/haoyiku/utils/privacy/PrivacyProxy"
        var hooksGlobalExclude = hooksTargetClassName.replace("/", ".")

        MethodHookBean(
            hooksTargetClassName,
            "getSimCountryIso",
            mutableSetOf(".*"),
            mutableSetOf(hooksGlobalExclude)
        ).let { hook ->
            hooks[getSimCountryIso] = hook
        }

        MethodHookBean(
            hooksTargetClassName,
            "getImeiOrImsi",
            mutableSetOf(".*"),
            mutableSetOf(hooksGlobalExclude)
        ).let { hook ->
            arrayOf(getImei, getDeviceId, getSubscriberId, getNai).forEach { source ->
                hooks[source] = hook
            }
        }

        MethodHookBean(
            hooksTargetClassName,
            "getAndroidId",
            mutableSetOf(".*"),
            mutableSetOf(hooksGlobalExclude)
        ).let { hook ->
            arrayOf(Secure_getString, System_getString).forEach { source ->
                hooks[source] = hook
            }
        }

        MethodHookBean(
            hooksTargetClassName,
            "getSharedPreferences",
            mutableSetOf(".*"),
            mutableSetOf(hooksGlobalExclude)
        ).let { hook ->
            hooks[getSharedPreferences] = hook
        }
        MethodHookBean(
            hooksTargetClassName,
            "getRunningAppProcesses",
            mutableSetOf(".*"),
            mutableSetOf(hooksGlobalExclude)
        ).let { hook ->
            hooks[getRunningAppProcesses] = hook
        }


    }

}