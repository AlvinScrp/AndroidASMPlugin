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
            //??????ISO???????????????????????????SIM??????????????????
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
     * ?????????????????????
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
                    macSerial = str.trim();//?????????
                    break;
                }
            }
        } catch (IOException ex) {
            // ???????????????
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
            //????????????:GSM????????? IMEI ??? CDMA????????? MEID.
            String deviceID = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????(???????????????????????????????????????????????????SIM????????????????????????)
            String tel = telephonyManager.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //????????????SIM???????????????
            String imei = telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //????????????id??????gsm??????imsi???
            String imsi = telephonyManager.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //????????????
            CellLocation str = telephonyManager.getCellLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //???????????????,????????????????????????????????????????????????,???CDMA??????????????????????????????
            String networkoperatorName = telephonyManager.getNetworkOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //??????????????????????????????????????????????????????
            String voiceMail = telephonyManager.getVoiceMailAlphaTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            //???????????????????????????
            String voiceMailNumber = telephonyManager.getVoiceMailNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            //?????????
//            //??????ISO???????????????????????????SIM??????????????????
//            String simCountryIso = telephonyManager.getSimCountryIso();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            //?????????
            String ss = telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            String sss = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ???????????????
             * 1.tm.CALL_STATE_IDLE=0          ?????????
             * 2.tm.CALL_STATE_RINGING=1  ??????
             * 3.tm.CALL_STATE_OFFHOOK=2  ??????

             */
            int callState = telephonyManager.getCallState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ???????????????????????????
             * ?????????the IMEI/SV(software version) for GSM phones.
             * Return null if the software version is not available.

             */
            String devicesoftware = telephonyManager.getDeviceSoftwareVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ??????ISO?????????????????????????????????????????????
             * ???????????????????????????????????????????????????
             *      ???CDMA?????????????????????????????????
             */
            String networkCountry = telephonyManager.getNetworkCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * MCC+MNC(mobile country code + mobile network code)
             * ???????????????????????????????????????????????????
             *    ???CDMA?????????????????????????????????

             */
//            String networkoperator = telephonyManager.getNetworkOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ??????????????????????????????
             * ????????? NETWORK_TYPE_UNKNOWN  ??????????????????  0

             NETWORK_TYPE_GPRS     GPRS??????  1

             NETWORK_TYPE_EDGE     EDGE??????  2

             NETWORK_TYPE_UMTS     UMTS??????  3

             NETWORK_TYPE_HSDPA    HSDPA??????  8

             NETWORK_TYPE_HSUPA    HSUPA??????  9

             NETWORK_TYPE_HSPA     HSPA??????  10

             NETWORK_TYPE_CDMA     CDMA??????,IS95A ??? IS95B.  4

             NETWORK_TYPE_EVDO_0   EVDO??????, revision 0.  5

             NETWORK_TYPE_EVDO_A   EVDO??????, revision A.  6

             NETWORK_TYPE_1xRTT    1xRTT??????  7

             */
            int netWorkType = telephonyManager.getNetworkType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ???????????????
             * ????????? PHONE_TYPE_NONE  ?????????

             PHONE_TYPE_GSM   GSM??????

             PHONE_TYPE_CDMA  CDMA??????

             */
            int phoneType = telephonyManager.getPhoneType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ??????SIM?????????????????????????????????????????????.5???6?????????????????????.
             * SIM????????????????????? SIM_STATE_READY(??????getSimState()??????).
             */
            String simOperator = telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ??????????????????
             * ??????????????????????????????
             * SIM????????????????????? SIM_STATE_READY(??????getSimState()??????).
             */
            String simOperatorName = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * SIM??????????????????
             * SIM_STATE_UNKNOWN          ???????????? 0

             SIM_STATE_ABSENT           ????????? 1

             SIM_STATE_PIN_REQUIRED     ??????????????????????????????PIN????????? 2

             SIM_STATE_PUK_REQUIRED     ??????????????????????????????PUK????????? 3

             SIM_STATE_NETWORK_LOCKED   ??????????????????????????????PIN????????? 4

             SIM_STATE_READY            ???????????? 5
             */
            int simStat = telephonyManager.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ICC???????????????

             */
            boolean bl = telephonyManager.hasIccCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????

            /**
             * ????????????:
             * (???GSM?????????)

             */
            boolean blean = telephonyManager.isNetworkRoaming();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            /**
             * ????????????????????????:
             * ?????????List<NeighboringCellInfo>
             * ???????????????android.Manifest.permission#ACCESS_COARSE_UPDATES
             */
            List<CellInfo> list = telephonyManager.getAllCellInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            /**
             * ????????????????????????

             */
            int dataActivty = telephonyManager.getDataActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????


            String sdsdsd = telephonyManager.getImei();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            String sdss = telephonyManager.getImei(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            String sdxxx = telephonyManager.getNai();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
            String meid = telephonyManager.getMeid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //?????????
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
