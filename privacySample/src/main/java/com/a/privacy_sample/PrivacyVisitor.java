package com.a.privacy_sample;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class PrivacyVisitor {

    public static String visitPrivacy(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nPrivacyVisitor.getIMSI_SubscriberId: " + PrivacyVisitor.getIMSI_SubscriberId(context));
        sb.append("\n\nPrivacyVisitor.getIMEI_DeviceId: " + PrivacyVisitor.getIMEI_DeviceId(context));
        sb.append("\n\nPrivacyVisitor.getIpAddress: " + PrivacyVisitor.getIpAddress(context));
        sb.append("\n\nPrivacyVisitor.getMacAddress1: " + PrivacyVisitor.getMacAddress(context));
        sb.append("\n\nPrivacyVisitor.getMacAddress2: " + PrivacyVisitor.getNewMac());
        sb.append("\n\nPrivacyVisitor.getSSID: " + PrivacyVisitor.getSSID(context));
        sb.append("\n\nPrivacyVisitor.getAndroidID: " + PrivacyVisitor.getAndroidID(context));
        sb.append("\n\nBuild.Serial By Filed: " + PrivacyVisitor.getSerialByFiled());
        sb.append("\n\nBuild.Serial By Method: " + PrivacyVisitor.getSerialByMethod());
        sb.append("\n\nPrivacyVisitor.getRunningAppProcesses: " + PrivacyVisitor.getRunningAppProcesses(context));
//        sb.append("\n\nNIMUtil.getProcessFromFile: " + PrivacyRefInvoke.invokeStaticMethod(NIMUtil.class, "getProcessFromFile"));
//        sb.append("\n\nNIMUtil.isMainProcessLive: " + NIMUtil.isMainProcessLive(context));
        sb.append("\n\ngetHostAddress: " + PrivacyVisitor.getHostAddress());
        sb.append("\n\ngetHostAddress: " + PrivacyVisitor.getRunningAppProcesses(context));
        sb.append("\n\ngetHostAddress2: " + PrivacyVisitor.getRunningAppProcesses2(context));
        sb.append("\n\nclipSetAndGet: " + PrivacyVisitor.clipSetAndGet(context));
        sb.append("\n\ngetActiveNetwork: " + getActiveNetwork(context));
        getSystemPhoneMessage(context);
        return sb.toString();

    }

    public static String getIMSI_SubscriberId(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                return telephonyManager.getSubscriberId();
//                sb.append("\n IMSI_SubscriberId:" + operatorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.getMessage());
        }
        return sb.toString();
    }


    public static String getSimCountryIso(Context mContext) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context
                    .TELEPHONY_SERVICE);
            //获取ISO国家码，相当于提供SIM卡的国家码。
            String simCountryIso = telephonyManager.getSimCountryIso();
            return simCountryIso;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAndroidID(Context mContext) {

        String androidID = "";
        try {
            return Settings.Secure.getString(mContext.getContentResolver(), "android_id");
        } catch (Exception e) {
            e.printStackTrace();
            return androidID;
        }
    }

        public static SharedPreferences getSharedPreferences(Context context) {
        try {
            return context.getSharedPreferences("sdsd", Context.MODE_MULTI_PROCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static String getIMEI_DeviceId(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                    .TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String operatorString = telephonyManager.getDeviceId();
                sb.append(operatorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public static String getSerialByFiled() {
        try {
            String serial = Build.SERIAL;
            return serial;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";


    }

    public static String getSerialByMethod() {
        try {
            String serial = Build.getSerial();
            return serial;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String getIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = (ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0.0.0";

    }

    public static String getSSID(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            return ssid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<unknown ssid>";

    }


    public static String getBSSID(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getBSSID();
            return ssid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<unknown ssid>";

    }

    public static String getMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String macAddress = wifiInfo.getMacAddress();
            return macAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";

    }

    public static String getActiveNetwork(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connectivityManager.getActiveNetwork();
            return network.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "getActiveNetwork";

    }


    /**
     * 通过网络接口取
     *
     * @return
     */
    public static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress3() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    public static String getHostAddress() {
        try {
            Enumeration var0 = NetworkInterface.getNetworkInterfaces();

            while (var0.hasMoreElements()) {
                NetworkInterface var1 = (NetworkInterface) var0.nextElement();
                Enumeration var2 = var1.getInetAddresses();

                while (var2.hasMoreElements()) {
                    InetAddress var3 = (InetAddress) var2.nextElement();
                    if (!var3.isLoopbackAddress() && var3 instanceof Inet4Address) {
                        return var3.getHostAddress();
                    }
                }
            }
        } catch (Exception var4) {
//            com.qiyukf.nimlib.k.b.e(a, "get ip address socket exception");
            var4.printStackTrace();
        }

        return "";
    }

    private static void getSystemPhoneMessage(Context context) {
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
            String deviceID = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号(有些手机号无法获取，是因为运营商在SIM中没有写入手机号)
            String tel = telephonyManager.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //获取手机SIM卡的序列号
            String imei = telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //获取客户id，在gsm中是imsi号
            String imsi = telephonyManager.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //电话方位
            CellLocation str = telephonyManager.getCellLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
            String networkoperatorName = telephonyManager.getNetworkOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //取得和语音邮件相关的标签，即为识别符
            String voiceMail = telephonyManager.getVoiceMailAlphaTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            //获取语音邮件号码：
            String voiceMailNumber = telephonyManager.getVoiceMailNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            //手机号
//            //获取ISO国家码，相当于提供SIM卡的国家码。
//            String simCountryIso = telephonyManager.getSimCountryIso();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            //手机号
            String ss = telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            String sss = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 电话状态：
             * 1.tm.CALL_STATE_IDLE=0          无活动
             * 2.tm.CALL_STATE_RINGING=1  响铃
             * 3.tm.CALL_STATE_OFFHOOK=2  摘机

             */
            int callState = telephonyManager.getCallState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 设备的软件版本号：
             * 例如：the IMEI/SV(software version) for GSM phones.
             * Return null if the software version is not available.

             */
            String devicesoftware = telephonyManager.getDeviceSoftwareVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 获取ISO标准的国家码，即国际长途区号。
             * 注意：仅当用户已在网络注册后有效。
             *      在CDMA网络中结果也许不可靠。
             */
            String networkCountry = telephonyManager.getNetworkCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * MCC+MNC(mobile country code + mobile network code)
             * 注意：仅当用户已在网络注册时有效。
             *    在CDMA网络中结果也许不可靠。

             */
//            String networkoperator = telephonyManager.getNetworkOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 当前使用的网络类型：
             * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0

             NETWORK_TYPE_GPRS     GPRS网络  1

             NETWORK_TYPE_EDGE     EDGE网络  2

             NETWORK_TYPE_UMTS     UMTS网络  3

             NETWORK_TYPE_HSDPA    HSDPA网络  8

             NETWORK_TYPE_HSUPA    HSUPA网络  9

             NETWORK_TYPE_HSPA     HSPA网络  10

             NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4

             NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5

             NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6

             NETWORK_TYPE_1xRTT    1xRTT网络  7

             */
            int netWorkType = telephonyManager.getNetworkType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 手机类型：
             * 例如： PHONE_TYPE_NONE  无信号

             PHONE_TYPE_GSM   GSM信号

             PHONE_TYPE_CDMA  CDMA信号

             */
            int phoneType = telephonyManager.getPhoneType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
             * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
             */
            String simOperator = telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 服务商名称：
             * 例如：中国移动、联通
             * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
             */
            String simOperatorName = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * SIM的状态信息：
             * SIM_STATE_UNKNOWN          未知状态 0

             SIM_STATE_ABSENT           没插卡 1

             SIM_STATE_PIN_REQUIRED     锁定状态，需要用户的PIN码解锁 2

             SIM_STATE_PUK_REQUIRED     锁定状态，需要用户的PUK码解锁 3

             SIM_STATE_NETWORK_LOCKED   锁定状态，需要网络的PIN码解锁 4

             SIM_STATE_READY            就绪状态 5
             */
            int simStat = telephonyManager.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * ICC卡是否存在

             */
            boolean bl = telephonyManager.hasIccCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号

            /**
             * 是否漫游:
             * (在GSM用途下)

             */
            boolean blean = telephonyManager.isNetworkRoaming();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            /**
             * 附近的电话的信息:
             * 类型：List<NeighboringCellInfo>
             * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
             */
            List<CellInfo> list = telephonyManager.getAllCellInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            /**
             * 获取数据连接状态

             */
            int dataActivty = telephonyManager.getDataActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号


            String sdsdsd = telephonyManager.getImei();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            String sdss = telephonyManager.getImei(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            String sdxxx = telephonyManager.getNai();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            String meid = telephonyManager.getMeid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //手机号
            String meid2 = telephonyManager.getMeid(1);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getRunningAppProcesses(Context context) {
        try {
//            new ArrayList();
            new ArrayList<ActivityManager.RunningAppProcessInfo>();
            ActivityManager.RunningAppProcessInfo info
                    = new ActivityManager.RunningAppProcessInfo();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            String processName = activityManager.getRunningAppProcesses().get(0).processName;
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<unknown process name>";
    }

    public static String clipSetAndGet(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setText("sdsdsdsd");
        ClipData clipData = clipboardManager.getPrimaryClip();
        return "clipSetAndGet";

    }

    public static String getRunningAppProcesses2(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
        List<ActivityManager.RunningTaskInfo> task22s = activityManager.getRunningTasks(100);

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        return "getRunningAppProcess2";
    }
}
