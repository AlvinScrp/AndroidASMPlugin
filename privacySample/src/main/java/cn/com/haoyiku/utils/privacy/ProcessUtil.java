package cn.com.haoyiku.utils.privacy;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.List;

public class ProcessUtil {
    private static String processName = null;

    public static String getProcessName(final Context context) {
        PrivacyLog.d("ProcessUtil.getProcessName");
        if (processName != null) {
            return processName;
        }
        //will not null
        processName = getProcessNameInternal(context);
        return processName;
    }

    private static String getProcessNameInternal(final Context context) {
        int myPid = android.os.Process.myPid();
        if (context == null || myPid <= 0) {
            return "";
        }
        String processName = getCurrentProcessNameByApplication();
        if (TextUtils.isEmpty(processName)) {
            processName = getCurrentProcessNameByActivityThread();
        }
        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByFile();
        }
        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByActivityManager(context);
        }
        if (TextUtils.isEmpty(processName)) {
            return "";
        }
        return processName;
    }

    private static String getProcessNameByActivityManager(Context context) {
        int myPid = android.os.Process.myPid();
        ActivityManager.RunningAppProcessInfo myProcess = null;
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            try {
                List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
                        .getRunningAppProcesses();

                if (appProcessList != null) {
                    for (ActivityManager.RunningAppProcessInfo process : appProcessList) {
                        if (process.pid == myPid) {
                            myProcess = process;
                            break;
                        }
                    }

                    if (myProcess != null) {
                        return myProcess.processName;
                    }
                }
            } catch (Exception e) {
                PrivacyLog.d("getProcessNameInternal exception:" + e.getMessage());
            }
        }
        return null;
    }

    @Nullable
    private static String getProcessNameByFile() {
        int myPid = android.os.Process.myPid();
        byte[] b = new byte[128];
        try (FileInputStream in = new FileInputStream("/proc/" + myPid + "/cmdline")) {
            int len = in.read(b);
            if (len > 0) {
                for (int i = 0; i < len; i++) { // lots of '0' in tail , remove them
                    if ((((int) b[i]) & 0xFF) > 128 || b[i] <= 0) {
                        len = i;
                        break;
                    }
                }
                return new String(b, 0, len);
            }

        } catch (Exception e) {
            PrivacyLog.d("getProcessNameInternal exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    public static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            final Method declaredMethod = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader())
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }


    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        return TextUtils.equals(getProcessName(context), context.getPackageName());
    }

}
