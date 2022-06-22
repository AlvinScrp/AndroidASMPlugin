package cn.com.haoyiku.utils.privacy

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings

/**
 * @author yanfang
 */
object PrivacyDataUtil {

    private const val SP_NAME = "privacy_id_cache"
    private const val KEY_DEVICE_ID = "android_id"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        val name = "${context.packageName}_$SP_NAME"
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun putSpString(
        context: Context,
        @Suppress("SameParameterValue") key: String,
        value: String
    ) {
        getSharedPreferences(context)
            .edit()
            .apply {
                this.putString(key, value)
            }.apply()
    }

    private fun getSpString(
        context: Context,
        @Suppress("SameParameterValue") key: String
    ): String? {
        return getSharedPreferences(context).getString(key, "")
    }

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getDeviceId(context: Context, resolver: ContentResolver, name: String): String {
        PrivacyLog.d("get Android Id")
        val key = KEY_DEVICE_ID
        val id = getSpString(context, key)
        val newId = if (id.isNullOrEmpty()) {
            return try {
                val cacheAndroidId = Settings.Secure.getString(resolver, name)
                putSpString(context, key = key, value = cacheAndroidId)
                cacheAndroidId
            } catch (var2: Throwable) {
                ""
            }
        } else id
        return newId
    }
}