package com.webuy.common.utils.privacy

import android.util.Log

object PrivacyRecord {
    private const val TAG = "privacy"

    @JvmStatic
    fun hooked(message: String?) {
        filterTrace(Log.getStackTraceString(Throwable("[Hooked]-->$message")))
            .let { Log.d(TAG, it) }
    }


    @JvmStatic
    fun log(message: String?) {
        filterTrace(Log.getStackTraceString(Throwable("[Log]-->$message")))
            .let { Log.w(TAG, it) }

    }

    @JvmStatic
    private fun filterTrace(stackTrace: String): String {
        return Log.getStackTraceString(Throwable(stackTrace))
            .replace("java.lang.Throwable: ","")
            .split("\n\t")
            .filterIndexed { index, _ -> index != 1 }
            .take(7).joinToString(separator = "\n\t", postfix = "\n\t")

    }
}
