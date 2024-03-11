package com.a.asm.sample;

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
        sb.append("\nSimCountryIso: " + getSimCountryIso(context));
        sb.append("\nAndroidID1: " + getSecureAndroidID(context));
        sb.append("\nAndroidID2: " + getSystemAndroidID(context));
        sb.append("\nDeviceId: " + getTelDeviceId(context));
        sb.append("\nSubscriberId: " + getTelSubscriberId(context));
        sb.append("\nImei: " + getTelIMEI(context));
        sb.append("\nTelNai: " + getTelNai(context));
        sb.append("\nIpAddress: " + getIpAddress(context));
        sb.append("\nMacAddress1: " + getMacAddress(context));
        sb.append("\nMacAddress2: " + getNewMac());
        sb.append("\nSSID: " + getSSID(context));
        sb.append("\nBuild.Serial By Filed: " + getSerialByFiled());
        sb.append("\nBuild.Serial By Method: " + getSerialByMethod());
        sb.append("\nRunningAppProcesses: " + getRunningAppProcesses(context));
        sb.append("\nHostAddress: " + getHostAddress());
        sb.append("\nHostAddress: " + getRunningAppProcesses(context));
        sb.append("\nHostAddress2: " + getRunningAppProcesses2(context));
        sb.append("\nclipSetAndGet: " + clipSetAndGet(context));
        sb.append("\nActiveNetwork: " + getActiveNetwork(context));
        getTelephonyManagerInfo(context);
        return sb.toString();

    }

    public static String getSimCountryIso(Context context) {
        try {
            //获取ISO国家码，相当于提供SIM卡的国家码。
            String simCountryIso = getTelM(context).getSimCountryIso();
            return simCountryIso;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String getSecureAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getSystemAndroidID(Context context) {
        return Settings.System.getString(context.getContentResolver(), "android_id");
    }

    public static String getTelDeviceId(Context context) {
        try {
            return getTelM(context).getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String getTelSubscriberId(Context context) {
        try {
            return getTelM(context).getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public static String getTelIMEI(Context context) {
        try {
            return getTelM(context).getImei();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String getTelNai(Context context) {
        try {
            return getTelM(context).getNai();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }




    private static TelephonyManager getTelM(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        try {
            return context.getSharedPreferences("sdsd", Context.MODE_MULTI_PROCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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

    private static void getTelephonyManagerInfo(Context context) {
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
            //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
            String deviceID = telephonyManager.getDeviceId(0);
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
        try {
            //手机号
            //获取ISO国家码，相当于提供SIM卡的国家码。
            String simCountryIso = telephonyManager.getSimCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            int callState = telephonyManager.getCallState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String devicesoftware = telephonyManager.getDeviceSoftwareVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String networkCountry = telephonyManager.getNetworkCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String networkoperator = telephonyManager.getNetworkOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int netWorkType = telephonyManager.getNetworkType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int phoneType = telephonyManager.getPhoneType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String simOperator = telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String simOperatorName = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int simStat = telephonyManager.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            boolean bl = telephonyManager.hasIccCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            boolean blean = telephonyManager.isNetworkRoaming();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<CellInfo> list = telephonyManager.getAllCellInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int dataActivty = telephonyManager.getDataActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sdsdsd = telephonyManager.getImei();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sdss = telephonyManager.getImei(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sdxxx = telephonyManager.getNai();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String meid = telephonyManager.getMeid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
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
