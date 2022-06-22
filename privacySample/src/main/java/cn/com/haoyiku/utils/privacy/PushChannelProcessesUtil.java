package cn.com.haoyiku.utils.privacy;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yanfang
 */
public class PushChannelProcessesUtil {

    public static String getProcessNameNew(Context context, int var0) {
      PrivacyLog.d("privacyUtil getProcessNameNew 进程 ");
        String var1 = "ps  |  grep  " + var0;
        BufferedReader var3 = null;
        DataOutputStream var4 = null;

        try {
            Process var2 = Runtime.getRuntime().exec("sh");
            var3 = new BufferedReader(new InputStreamReader(var2.getInputStream()));
            var4 = new DataOutputStream(var2.getOutputStream());
            var4.writeBytes(var1 + "  &\n");
            var4.flush();
            var4.writeBytes("exit\n");
            var2.waitFor();

            String var5;
            while ((var5 = var3.readLine()) != null) {
                var5 = var5.replaceAll("\\s+", "  ");
                String[] var6 = var5.split("  ");
                if (var6.length >= 9 && !TextUtils.isEmpty(var6[1]) && var6[1].trim().equals(String.valueOf(var0))) {
                    String var7 = var6[8];
                    return var7;
                }
            }
        } catch (Exception var18) {
            PrivacyLog.d(var18.getMessage());
        } finally {
            try {
                if (var3 != null) {
                    var3.close();
                }

                if (var4 != null) {
                    var4.close();
                }
            } catch (IOException var17) {
                var17.printStackTrace();
                PrivacyLog.d(var17.getMessage());
            }

        }
        return "";
    }
}
