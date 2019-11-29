package com.sample.loginkit.utils;

import android.util.Log;

import com.sample.loginkit.init.RootLoginController;


/*
Common class for overriding Logs to enable or disable them
 */
public class LogUtils {

    public static void debug(final String tag, String message) {
        if (RootLoginController.customConfiguration.isLoggingEnable) {
            Log.d(tag, message);
        }
    }
}
