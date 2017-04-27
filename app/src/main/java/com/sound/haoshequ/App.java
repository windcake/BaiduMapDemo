package com.sound.haoshequ;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudManager;

/**
 * 创建者： windcake
 * 创建时间： 17/4/26下午11:01.
 */
public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        SDKInitializer.initialize(getApplicationContext());

    }
}
