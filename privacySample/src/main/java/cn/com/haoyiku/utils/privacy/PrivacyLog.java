package cn.com.haoyiku.utils.privacy;

import android.util.Log;

public class PrivacyLog {

    public static final String TAG = "privacy";

    public static void d(String message) {
        Log.d(TAG, Log.getStackTraceString(new Throwable(message)));
//        Log.d(TAG,"message",new Throwable(message));
        Log.d(TAG,"sds");
    }

    public static void w(String message) {
        Log.w("alvin", Log.getStackTraceString(new Throwable(message)));
//        Log.d(TAG,"message",new Throwable(message));
        Log.w("alvin","eeeeeeeeeee");
    }

}
