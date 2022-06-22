package cn.com.haoyiku.utils.privacy;

import android.util.Log;

public class PrivacyLog {

    public static final String TAG = "privacy";

    public static void d(String message) {
        Log.d(TAG, Log.getStackTraceString(new Throwable(message)));
    }

}
