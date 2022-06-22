package cn.com.haoyiku.utils.privacy;

import android.content.ContentResolver;
import android.provider.Settings;

import com.a.privacy_sample.HApp;

/**
 * @author yanfang
 */
public class AndroidIdUtil {

    public static String getAndroidId(ContentResolver resolver, String name) {
        if ("android_id".equals(name)) {
            PrivacyLog.d("privacyUtil getAndroidId: 是");
            return PrivacyDataUtil.getDeviceId(HApp.context, resolver, name);
        } else {
            PrivacyLog.d("privacyUtil getAndroidId:  不是");
            try {
                return Settings.Secure.getString(resolver, name);
            } catch (Throwable e) {
                return "";
            }
        }
    }
}
