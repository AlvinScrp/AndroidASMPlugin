package cn.com.haoyiku.utils.privacy;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.a.privacy_sample.HApp;


/**
 * @author yanfang
 * @data
 * @layout
 * @description
 */
public class TelephonyManagerUtil {
    public static String getSimCountryIso() {
        Context context = HApp.context;
        boolean agreed = true;
        if (agreed) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    String simCountryIso = telephonyManager.getSimCountryIso();
                    PrivacyLog.d("TelephonyManagerUtil getSimCountryIso: " + simCountryIso);
                    return simCountryIso;
                }
            } catch (Throwable e) {
                return "CN";
            }
        }
        PrivacyLog.d("TelephonyManagerUtil getSimCountryIso: " + agreed);
        return "CN";
    }
}
