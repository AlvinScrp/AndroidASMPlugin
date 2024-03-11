package com.webuy.common.utils.privacy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import androidx.annotation.NonNull;
import com.tencent.mmkv.MMKV;

public class PrivacyHook {


    public static String getSecureAndroidId(ContentResolver resolver, String name) {
        return getSettingString(resolver, name, false);
    }

    public static String getSystemAndroidId(ContentResolver resolver, String name) {
        return getSettingString(resolver, name, true);
    }

    private static String getSettingString(ContentResolver resolver, String name, Boolean isSystem) {
        try {
            boolean isAndroidId = "android_id".equals(name);
            PrivacyRecord.hooked("PrivacyHook is getSystemAndroidId: :" + (isAndroidId) + name);
            if (isAndroidId) {
                return "android id fake value";
            }
            return isSystem
                    ? Settings.System.getString(resolver, name)
                    : Settings.Secure.getString(resolver, name);
        } catch (Throwable e) {
            return "";
        }
    }




    public static String getSimCountryIso(TelephonyManager telephonyManager) {
        return "cn";
    }


    public static String getDeviceId(TelephonyManager telephonyManager) {
        PrivacyRecord.hooked("PrivacyHook getDeviceId");
        return "hook DeviceId";
    }

    public static String getSubscriberId(TelephonyManager telephonyManager) {
        PrivacyRecord.hooked("PrivacyHook getSubscriberId");
        return "hook SubscriberId";
    }

    public static String getImei(TelephonyManager telephonyManager) {
        PrivacyRecord.hooked("PrivacyHook getImei");
        return "hook imei";
    }

    public static String getNai(TelephonyManager telephonyManager) {
        PrivacyRecord.hooked("PrivacyHook getNai");
        return "hook nai";
    }

    public static String getProcessNameNew(Context context, int var0) {
        PrivacyRecord.hooked("PrivacyHook getProcessNameNew 进程 ");
        return " ProcessName new value";
    }

    public static SharedPreferences getHookedSharedPreferences(Context context, String name,int mode) {
        PrivacyRecord.hooked("PrivacyHook getSharedPreferences:"+name);
        initIfNeed(context);
        return MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE);
    }

    private static boolean isInit = false;

    public static void initIfNeed(@NonNull Context context) {
        if (!isInit) {
            Context applicationContext = context.getApplicationContext();
            MMKV.initialize(applicationContext, applicationContext.getFilesDir().toString());
            isInit = true;
        }
    }
}
