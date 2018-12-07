package com.example.wireless;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.TErrorCode;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.overlay.PolygonOverlay;
import com.tianditu.android.maps.renderoption.PlaneOption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bean.LocationWebChromeClient;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.SharedPreferencesUtils;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public LocationClient mLocationClient = null;
    boolean isExit;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private WebView webView;
    StringBuilder currentPosition = new StringBuilder();
    StringBuilder currentPosition2 = new StringBuilder();
    private String JingDu;//经度
    private String WeiDu;//纬度
    public String getID;
    public String getFlag;
    String sessionid = "";
//    private com.tianditu.android.maps.MapView mapViews;
    private MapView mapViews;
    MyLocationOverlay myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注意该方法要再setContentView方法之前实现
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
        String date = getTime(new Date());
        Log.d("12345654321", "时间格式:" + date);
        boolean today = rs.getBoolean(""+ date,true);
        if (today){
            rs.putValues(new SharedPreferencesUtils.ContentValue("index"+ getTime(new Date()),0),
            new SharedPreferencesUtils.ContentValue(""+date,false));
        }
        //地图逻辑
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new Main2Activity.MyLocationListener());
        requestLocation();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Main2Activity.this, permissions, 1);
            
        } else {

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
//        start();
        mapViews = (MapView) findViewById(R.id.mapview);
        mapViews.setBuiltInZoomControls(true);
        MapController mapController = mapViews.getController();
//        int jingdu = Integer.parseInt(JingDu);
//        int weidu = Integer.parseInt(WeiDu);
        myLocation = new MyLocationOverlay(this,mapViews);
        myLocation.enableCompass();
        myLocation.enableMyLocation();
        GeoPoint points = myLocation.getMyLocation();
        mapViews.addOverlay(myLocation);
        TGeoDecode tGeoDecode = new TGeoDecode(new OnGeoResultListener());
        tGeoDecode.search(points);
        GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
        mapController.setCenter(points);
        mapController.setZoom(15);
        //标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //悬浮菜单栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String a = ""+  rs.getString("xunchabianhao"+ getTime(new Date()));
        String ssss = "null";

        //事件及巡查按钮实现
        com.ddz.floatingactionbutton.FloatingActionButton event_button = (com.ddz.floatingactionbutton.FloatingActionButton)findViewById(R.id.ShiJian);
        event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = ""+  rs.getString("xunchabianhao"+ getTime(new Date()));
                String ssss = "null";
                if (a.equals(ssss)){
                    showToast("尚未提交巡查记录！");
                }else {
                    Intent intent = new Intent();
                    intent.setClass(Main2Activity.this,AddEvent2Activity.class);
                    intent.putExtra("userid",getID);
                    startActivity(intent);
                }


            }
        });
        com.ddz.floatingactionbutton.FloatingActionButton record_button = (com.ddz.floatingactionbutton.FloatingActionButton)findViewById(R.id.XunCha);
        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this,AddRecordActivity.class);
                intent.putExtra("userid",getID);
                startActivity(intent);

            }
        });
    }
    //获取经纬度方法
    //获取并将经纬度填入
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            currentPosition2.append(location.getLatitude());
            currentPosition.append(location.getLongitude());
            WeiDu= currentPosition2.toString();
            JingDu = currentPosition.toString();
        }
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main2Activity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getTime(Date date){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(date);
        return ee;
    }
    private void start(){
//        webView = (WebView) findViewById(R.id.web_view);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setDomStorageEnabled(true);
//
////        webView.getSettings().setDatabaseEnabled(true);
////        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
////        webView.getSettings().setGeolocationEnabled(true);
////        webView.getSettings().setGeolocationDatabasePath(dir);
////        webView = (WebView) findViewById(R.id.web_view);
////        webView.getSettings().setJavaScriptEnabled(true);
////        webView.getSettings().setUseWideViewPort(true);
//        webView.setWebChromeClient(new LocationWebChromeClient(this));
//
////        webView.getSettings().setUserAgentString("app/网格化事件上报系统");
////        String ua = webView.getSettings().getUserAgentString();
////        webView.getSettings().setUserAgentString(ua+"_android");
////        Log.d("12345654321", "UA格式:" + webView.getSettings().getUserAgentString());
//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setSupportMultipleWindows(false);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
//        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//        webView.getSettings().setGeolocationEnabled(true);
//        webView.getSettings().setGeolocationDatabasePath(dir);
////        webView.setWebChromeClient(new WebChromeClient());
////        webView.setWebChromeClient(new MyWbChromeClient());
////        webView.loadUrl("file:///android_res/raw/index.html");
//        webView.loadUrl("http://www.sdmap.gov.cn/tdtMobile/pages/#/map");
//        webView.loadUrl("http://www.sdmap.gov.cn/map.html");
//

    }
    //开始地图周期
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    //获取当前位置并打印
//    public class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // 只是完成了定位
//            MyLocationData locData = new MyLocationData.Builder().latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//
//            //设置图标在地图上的位置
//            baiduMap.setMyLocationData(locData);
//
//            // 开始移动百度地图的定位地点到中心位置
//            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
//            baiduMap.animateMapStatus(u);
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }


    @Override

    protected void onResume() {

        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理

        //mMapView.onResume();

    }

    @Override

    protected void onPause() {

        super.onPause();

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理

        //mMapView.onPause();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //实现菜单栏的跳转
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //主页
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            //我的事件（网格员权限）
            Intent intent = new Intent();
            intent.setClass(Main2Activity.this,MyEventsActivity.class);
            intent.putExtra("userid",getID);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            //全部事件（网格长权限）
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this,AllEventsActivity.class);
                startActivity(intent);


        } else if(id == R.id.caogaoxinag){
            Intent intent = new Intent();
            intent.setClass(Main2Activity.this,CaoGaoXiang.class);
            intent.putExtra("userid",getID);
            startActivity(intent);

        }else if (id == R.id.nav_manage) {

            //设置（待定）
            File file = getCacheDir();
            deleteFileByDirectory(file);
            Toast.makeText(Main2Activity.this,"缓存清理完成",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            //关于软件
            Intent intent = new Intent();
            intent.setClass(Main2Activity.this,EReportActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_send) {
            //退出登录
            //改
            //android.os.Process.killProcess(android.os.Process.myPid());

            Intent intent = new Intent();
            intent.setClass(Main2Activity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }
//        else if (id == R.id.updateversion){
//
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //重载函数以实现再按一次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                System.exit(0);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }

    };

    private static void deleteFileByDirectory(File directory){
        if(directory != null && directory.exists() && directory.isDirectory()){
            for (File item :directory.listFiles()){
                item.delete();
            }
        }
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /*
    *
    * 获取最新版本号
    * */
    String version;
    public String getUpdateVersion(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("","")
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("").post(requestBody).build();
                    Response response = okHttpClient.newCall(request).execute();
                    version = response.body().string();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return version;
    }

    public void downapk(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    private class MyWbChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);

        }
    }
    /**
     * 逆地理编码回调结果监听
     */
    class OnGeoResultListener implements TGeoDecode.OnGeoResultListener {

        @Override
        public void onGeoDecodeResult(TGeoAddress tGeoAddress, int errorCode) {

            if (TErrorCode.OK == errorCode) {
                // 查询点相关信息
                String str = "最近的 poi 名称:" + tGeoAddress.getPoiName() + "\n";
                str += "查询点 Poi 点的方位:" + tGeoAddress.getPoiDirection() + "\n";
                str += "查询点 Poi 点的距离:" + tGeoAddress.getPoiDistance() + "\n";
                str += "查询点行政区名称:" + tGeoAddress.getCity() + "\n";
                str += "查询点地理描述全称:" + tGeoAddress.getFullName() + "\n";
                str += "查询点的地址:" + tGeoAddress.getAddress() + "\n";
                str += "查询点的方位:" + tGeoAddress.getAddrDirection() + "\n";
                str += "查询点的距离:" + tGeoAddress.getAddrDistance() + "\n";
                str += "查询点道路名称:" + tGeoAddress.getRoadName() + "\n";
                str += "查询点与最近道路的距离:" + tGeoAddress.getRoadDistance();
                System.out.println(str);
            } else {
                System.out.println("查询出错：" + errorCode);
            }
        }
    }
}
