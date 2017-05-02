package com.sound.haoshequ;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.sound.haoshequ.autolayout.AutoLayoutActivity;
import com.sound.haoshequ.net.OkGo;
import com.sound.haoshequ.net.callback.StringCallback;
import com.sound.haoshequ.utils.L;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements CloudListener,SensorEventListener,View.OnClickListener
{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button button;
//  定位相关

    private LocationClient mLocClient;
    private BitmapDescriptor mCurrentMarker;

    private  MyLocationListenner myListenner = new MyLocationListenner();

    private SensorManager mSensorManager;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private MyLocationData locData;
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OkGo.get("https://www.baidu.com").execute(new StringCallback()
        {
            @Override
            public void onSuccess(String s, Call call, Response response)
            {
                Log.i("aaaa","网络访问返回数据：" + s);
            }
        });

        mMapView = (MapView) findViewById(R.id.mapview);
//        button = (Button) findViewById(R.id.search_button);
        mBaiduMap = mMapView.getMap();
//      传感器服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//      开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
//      定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListenner);

        LocationClientOption option = new LocationClientOption();
//      打开GPS
        option.setOpenGps(true);
//      设置坐标类型
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
//      开启定位
        mLocClient.start();


        CloudManager.getInstance().init(this);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                LocalSearchInfo info = new LocalSearchInfo();
//                info.ak = "B266f735e43ab207ec152deff44fec8b";
//                info.geoTableId = 31869;
//                info.tags = "";
//                info.q = "天安门";
//                info.region = "北京市";
//                CloudManager.getInstance().localSearch(info);
//
//                NearbySearchInfo nearbyInfo  = new NearbySearchInfo();
//                nearbyInfo.location = "116.4321,38.76623";
//                CloudManager.getInstance().nearbySearch(nearbyInfo);
//
////                InfoWindow infoWindow = new InfoWindow();
////                mBaiduMap.showInfoWindow();
//
//
//            }
//        });

    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
//        Log.i("aaaaa","GetSearchResult:" + cloudSearchResult.poiList.toString());

        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            Log.i("aaaa","大小是：" + result.poiList.size());

            L.i(result.toString() + "List的String:" + result.poiList.get(0).extras.get(0).toString());


            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            LatLng ll;

            LatLngBounds.Builder latBuilder = new LatLngBounds.Builder();

            for (CloudPoiInfo info : result.poiList)
            {
//              坐标
                ll = new LatLng(info.latitude,info.longitude);

                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);

                mBaiduMap.addOverlay(oo);

                latBuilder.include(ll);

            }

            LatLngBounds bounds = latBuilder.build();
            MapStatusUpdate u  = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);

        }


    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
            Log.i("aaaaaa","GetDetailSearchResult:" + detailSearchResult.poiInfo.toString());
    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
            Log.i("aaaaa","GetCloudRgcResult:" + cloudRgcResult.toString());
    }






    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_SHORT).show();


//        LocalSearchInfo info = new LocalSearchInfo();
//                info.ak = "KnO73NauV1k7TRWaQMqSKx79ZHbGN5BC";
//                info.geoTableId = 167258;
//                info.tags = "";
//                info.q = "锦秋国际";
//                info.region = "北京市";
//                CloudManager.getInstance().localSearch(info);

                NearbySearchInfo nearbyInfo  = new NearbySearchInfo();
                nearbyInfo.ak = "KnO73NauV1k7TRWaQMqSKx79ZHbGN5BC";
                nearbyInfo.geoTableId = 167258;
                nearbyInfo.radius = 30000;
                nearbyInfo.location = "116.35537,39.981559";
                CloudManager.getInstance().nearbySearch(nearbyInfo);

//                InfoWindow infoWindow = new InfoWindow();
//                mBaiduMap.showInfoWindow();



    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }



}
