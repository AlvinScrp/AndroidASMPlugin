package com.a.privacy_sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import cn.com.haoyiku.utils.privacy.PrivacyProxy;

public class Hello {

    public static String a() {
        Hello hello = new Hello();
        String ans = hello.c("s1", "s2");
        return ans;
//        return "sdsd";
    }

    public String b() {
//        PrivacyProxy.getSharedPreferences(HApp.context,"sdsd",Context.MODE_MULTI_PROCESS);
        return "b";
    }

    public String c(String str1, String str2) {
        return str1 + str2;
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

}
