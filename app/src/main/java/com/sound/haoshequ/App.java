package com.sound.haoshequ;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudManager;
import com.sound.haoshequ.autolayout.config.AutoLayoutConifg;
import com.sound.haoshequ.net.OkGo;

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
//      百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
//      网络访问工具初始化
        OkGo.init(this);
//      AutoLayout初始化
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

    }
}
