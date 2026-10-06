package com.instaguard.user.Helpers;

import android.util.Log;

/**
 * Created by admin on 18/07/17.
 */

public class Logger {
    private static boolean isDebugMode=true;
    private static String TAG="com.instaguard.user";
    public static void printDebug(String value)
    {
        if(isDebugMode)
        {
            Log.d(TAG,value);
        }
    }
    public static void printError(String value)
    {
        if(isDebugMode)
        {
            Log.e(TAG,value);
        }
    }
    public static void printInfo(String value)
    {
        if(isDebugMode)
        {
            Log.i(TAG,value);
        }
    }
}
