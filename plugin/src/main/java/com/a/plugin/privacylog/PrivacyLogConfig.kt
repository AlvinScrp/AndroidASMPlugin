package com.a.plugin.privacylog

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * 参考
 * https://github.com/yanerchuang/PrivacyPolicyComplianceCheck/blob/master/app/src/main/java/com/ywj/xposeddemo/MainActivity.java
 * TelephonyManager信息获取：https://www.cnblogs.com/weixing/p/3253479.html
 */
object PrivacyLogConfig {

    var opCode = mutableMapOf(
        Opcodes.INVOKESTATIC to "INVOKESTATIC",
        Opcodes.INVOKEVIRTUAL to "INVOKEVIRTUAL",
        Opcodes.GETSTATIC to "GETSTATIC"

    )

    //    var Statement_log = "INVOKESTATIC cn/com/haoyiku/utils/privacy/PrivacyLog.d (Ljava/lang/String;)V"
    var logMethodNode =
        MethodInsnNode(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;)I")
    var ignoreClasses: MutableSet<String> = mutableSetOf("cn.com.haoyiku.HykApp")
    var ignorePackages: MutableSet<String> =
        mutableSetOf(
            "kotlin.*",
            "kotlinx.*",
            "com.google.*",
            "org.jetbrains.*",
            "org.objectweb.*",
            "org.intellij.*",
            "androidx.*",
            "android.*",
            "io.flutter.*",
            "com.ta.a.d.e",//full path class
            "com.ta.utdid2.device.c",//full path class
            "com.idlefish.*"
        )
    var checkClasses = mutableSetOf(
        "android/telephony/TelephonyManager",
        "android/net/wifi/WifiManager",
        "android/net/ConnectivityManager",
        "android/net/wifi/WifiInfo",
        "java/net/NetworkInterface",
        "java/lang/Runtime.exec",
        "android/app/ActivityManager",
        "android/content/pm/PackageManager",
        "android/provider/Settings\$Secure",
        "android/provider/Settings\$System",
        "android/content/ClipboardManager",
        "android/os/Build"
    )
    var methodSet: MutableSet<String>
    var fieldSet: MutableSet<String> = mutableSetOf()
//    var methodHookValueSet: MutableSet<String> = mutableSetOf()
//    var fieldHookValueSet: MutableSet<String> = mutableSetOf()

    //不可变设备信息 IMSI、IMEI，MEID ,SimSerialNumber,设备手机号，MAC地址
    var getSerial = "INVOKESTATIC android/os/Build.getSerial ()Ljava/lang/String;" //设备序列号
    var getSubscriberId =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSubscriberId ()Ljava/lang/String;" //IMSI
    var getDeviceId =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getDeviceId ()Ljava/lang/String;" //IMEI
    var getImei =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getImei ()Ljava/lang/String;" //IMEI
    var getImei2 =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getImei (I)Ljava/lang/String;" //IMEI
    var getNai =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNai ()Ljava/lang/String;" //IMEI
    var getMeid =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getMeid ()Ljava/lang/String;" //CDMA手机的 MEID
    var getMeid2 =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getMeid (I)Ljava/lang/String;" //CDMA手机的 MEID
    var getSimSerialNumber =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimSerialNumber ()Ljava/lang/String;" //SIM卡的序列号
    var getLine1Number =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getLine1Number ()Ljava/lang/String;" //手机号
    var getCellLocation =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getCellLocation ()Landroid/telephony/CellLocation;" //电话方位
    var getNetworkOperatorName =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNetworkOperatorName ()Ljava/lang/String;" //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
    var getNetworkOperator =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNetworkOperator ()Ljava/lang/String;" //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
    var getNetworkType =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNetworkType ()I" //当前使用的网络类型
    var getNetworkCountryIso =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getNetworkCountryIso ()Ljava/lang/String;" //获取ISO标准的国家码，即国际长途区号。
    var getPhoneType =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getPhoneType ()I" //手机类型 NONE GSM CDMA
    var getVoiceMailNumber =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getVoiceMailNumber ()Ljava/lang/String;" //获取语音邮件号码
    var getVoiceMailAlphaTag =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getVoiceMailAlphaTag ()Ljava/lang/String;" //获取语音邮件号码
    var getSimCountryIso =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimCountryIso ()Ljava/lang/String;" //获取ISO国家码，相当于提供SIM卡的国家码
    var getSimOperator =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimOperator ()Ljava/lang/String;" //获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
    var getSimOperatorName =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimOperatorName ()Ljava/lang/String;" //
    var getSimState =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getSimState ()I" //
    var getAllCellInfo =
        "INVOKEVIRTUAL android/telephony/TelephonyManager.getAllCellInfo ()Ljava/util/List;" //附近的电话信息
    var hasIccCard = "INVOKEVIRTUAL android/telephony/TelephonyManager.hasIccCard ()Z" //ICC卡是否存在


    //WLAN接入点，IP地址,MAC地址
    var getConnectionInfo =
        "INVOKEVIRTUAL android/net/wifi/WifiManager.getConnectionInfo ()Landroid/net/wifi/WifiInfo;" //getConnectionInfo
    var getActiveNetworkInfo =
        "INVOKEVIRTUAL android/net/ConnectivityManager.getActiveNetwork ()Landroid/net/Network;" //getActiveNetworkInfo
    var getMacAddress =
        "INVOKEVIRTUAL android/net/wifi/WifiInfo.getMacAddress ()Ljava/lang/String;" //mac 地址
    var getSSID = "INVOKEVIRTUAL android/net/wifi/WifiInfo.getSSID ()Ljava/lang/String;" //WLAN接入点
    var getBSSID = "INVOKEVIRTUAL android/net/wifi/WifiInfo.getBSSID ()Ljava/lang/String;" //WLAN接入点
    var getIpAddressByWifiInfo = "INVOKEVIRTUAL android/net/wifi/WifiInfo.getIpAddress ()I" //ip
    var getNetworkInterfaces =
        "INVOKESTATIC java/net/NetworkInterface.getNetworkInterfaces ()Ljava/util/Enumeration;" //ip
    var getHardwareAddress =
        "INVOKEVIRTUAL java/net/NetworkInterface.getHardwareAddress ()[B" //mac地址

    // LDC "cat/sys/class/net/wlan0/address"
    var getMacRuntimeExec =
        "INVOKEVIRTUAL java/lang/Runtime.exec (Ljava/lang/String;)Ljava/lang/Process;" //mac地址

    //软件列表
    var getRunningAppProcesses =
        "INVOKEVIRTUAL android/app/ActivityManager.getRunningAppProcesses ()Ljava/util/List;" //运行进程
    var getAppTasks =
        "INVOKEVIRTUAL android/app/ActivityManager.getAppTasks ()Ljava/util/List;" //运行进程
    var getRunningTasks =
        "INVOKEVIRTUAL android/app/ActivityManager.getRunningTasks (I)Ljava/util/List;" //运行进程
    var getInstalledPackages =
        "INVOKEVIRTUAL android/content/pm/PackageManager.getInstalledPackages (I)Ljava/util/List;" //安装应用列表
    var getInstalledApplications =
        "INVOKEVIRTUAL android/content/pm/PackageManager.getInstalledApplications (I)Ljava/util/List;" //安装应用列表

    //Android ID 还需要检测方法参数为 "android_id"
    var Secure_getString =
        "INVOKESTATIC android/provider/Settings\$Secure.getString (Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;" //Android Id?
    var System_getString =
        "INVOKESTATIC android/provider/Settings\$System.getString (Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;" //Android Id?
    var setClipText =
        "INVOKEVIRTUAL android/content/ClipboardManager.setText (Ljava/lang/CharSequence;)V" //剪切板
    var getPrimaryClip =
        "INVOKEVIRTUAL android/content/ClipboardManager.getPrimaryClip ()Landroid/content/ClipData;" //剪切板


    //    var Field_Serial =
//        FieldInsnNode(Opcodes.GETSTATIC, "android/os/Build", "SERIAL", "Ljava/lang/String;")
    var Field_Serial = "GETSTATIC android/os/Build.SERIAL : Ljava/lang/String;"

    init {
        methodSet = mutableSetOf(
            getSerial,
            getSubscriberId,
            getDeviceId,
            getImei,
            getImei2,
            getNai,
            hasIccCard,
            getMeid,
            getMeid2,
            getSimSerialNumber,
            getLine1Number,
            getNetworkOperatorName,
            getNetworkOperator,
            getNetworkType,
            getNetworkCountryIso,
            getSimOperator,
            getSimOperatorName,
            getSimCountryIso,
            getSimState,
            getVoiceMailNumber,
            getVoiceMailAlphaTag,
            getPhoneType,
            getAllCellInfo,
            getCellLocation,
            getMacAddress,
            getSSID,
            getBSSID,
            getConnectionInfo,
            getActiveNetworkInfo,
            getIpAddressByWifiInfo,
            getNetworkInterfaces,
            getHardwareAddress,
            getMacRuntimeExec,
            getRunningAppProcesses,
            getAppTasks,
            getRunningTasks,
            getInstalledPackages,
            getInstalledApplications,
            Secure_getString,
            System_getString,
            getPrimaryClip,
            setClipText
        )
        fieldSet = mutableSetOf(Field_Serial)
//        methodHookValueSet = mutableSetOf(
//            getSerial,
//            getSubscriberId,
//            getDeviceId,
//            getImei,
//            getImei2,
//            getNai,
//            getMeid,
//            getMeid2,
//            getSimSerialNumber,
//            getMacAddress,
//            getHardwareAddress,
//            Secure_getString,
//            System_getString
//        )
//        fieldHookValueSet = mutableSetOf(Field_Serial)

    }

}