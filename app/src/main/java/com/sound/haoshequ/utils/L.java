package com.sound.haoshequ.utils;

import android.util.Log;

/**
 * Created by zhy on 15/11/18.
 */
public class L
{
    public static boolean debug = true;
    private static final String TAG = "aaaaa";

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg)
    {
        if (debug)
        {
            Log.i(TAG, msg);
        }
    }



}
