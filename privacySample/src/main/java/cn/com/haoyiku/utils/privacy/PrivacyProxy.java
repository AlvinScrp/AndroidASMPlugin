package cn.com.haoyiku.utils.privacy;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.a.privacy_sample.HApp;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class PrivacyProxy {

    /**
     * @param telephonyManager
     * @return
     */
    public static String getSimCountryIso(TelephonyManager telephonyManager) {
        return "getSimCountryIso~~~";
    }

    public static String getImeiOrImsi(TelephonyManager telephonyManager) {
        return "getImeiOrImsi~~~";
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(ActivityManager activityManager) {
        PrivacyLog.d("getRunningAppProcesses");
        return new ArrayList<>();
    }

    public static String getAndroidId(ContentResolver resolver, String name) {
        if ("android_id".equals(name)) {
            PrivacyLog.d("privacyUtil getAndroidId: 是");
            return "androidid~~~~";
        } else {
            PrivacyLog.d("privacyUtil getAndroidId:  不是");
            try {
                return Settings.Secure.getString(resolver, name);
            } catch (Throwable e) {
                return "";
            }
        }
    }

    public static SharedPreferences getSharedPreferences(Context context, String name, int mode) {
        if (mode == 4) {
            PrivacyLog.d("getSharedPreferences");
            initIfNeed(context);
            return MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE);
        } else {
            return context.getSharedPreferences(name, mode);
        }
    }

    private static boolean isInit = false;

    private static void initIfNeed(Context context) {
        if (!isInit) {
            MMKV.initialize(
                    context.getApplicationContext(),
                    context.getApplicationContext().getFilesDir().toString()
            );
            isInit = true;
        }
    }

}
