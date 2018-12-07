package com.example.wireless;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.jtds.jdbc.DateTime;

import DataBaseUtil.MyDatebaseHelper;
import MyGridView.MyGridView;
import bean.RecordData;
import bean.ShiJianXinXi;
import dialog.MyDialog;
import myapp.MyApplication;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import util.SharedPreferencesUtils;
import util.Until;
import bean.GetPathFromUri4kitkat;

public class AddEvent2Activity extends Activity {
    Context mContext;
    String path = "";
    File out;
    //region 暂时无关

    //数据库相关
    private MyDatebaseHelper datebaseHelper = new MyDatebaseHelper(this,"WangGe.db",null,3);
    private Integer count;

    //地图经纬度相关
    public LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    String happentime;
    String xunchashijian;
    private Uri image;
    //时间相关
    private EditText time;
    //获取上报时间的方法
    private void getTime(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        CharSequence cs = null;
        cs = ee;
        happentime= cs.toString();
    }
    //获取巡查记录编号的方法
    private void getTime2(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyyMMdd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        CharSequence cs = null;
        cs = ee;
        xunchashijian = cs.toString();
    }
    //获取事件发生编号的方法
    private void getTime3(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyyMMdd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        CharSequence cs = null;
        cs = ee;
        xunchashijian = cs.toString();
    }

    //endregion
    //拍照与选择照片相关
    private MyDialog dialog;// 图片选择对话框
    public static final int NONE = 0;
    public static final int PHOTO = 0;
    public static final int XIANGCE = 1;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMER = 2;
    private GridView gridView; // 网格显示缩略图
    private final int IMAGE_OPEN = 4; // 打开图片标记
    private String pathImage; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    public List<Bitmap> imgList = new ArrayList<Bitmap>();;
    public MyAdapter myAdapter;
    public boolean isShowDelete = false;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    public ImageView ivDelete;
    private SimpleAdapter simpleAdapter; // 适配器

    StringBuilder currentPosition = new StringBuilder();
    StringBuilder currentPosition2 = new StringBuilder();

    //region 暂时无关定义
    String weidu2 ;
    String jingdu2 ;
    String shangbao01;
    Spinner leixing;
    Spinner xiaolei;
    String [] shijianleixing = new String[]{"综治维稳","公共安全","生态环保"};
    String [] xiaolei1 = new String[]{"矛盾纠纷","信访","重点人员","特殊人群","治安邢事案件","突发问题"};
    String [] xiaolei2 = new String[]{"非法违法生产经营活动","道路交通","火灾","建筑施工","电梯","校车"};
    String [] xiaolei3 = new String[]{"空气","水","环境生态","噪声"};

    ArrayAdapter<String> adapter ;
    ArrayAdapter<String> adapter2 ;
    String sql = "";

    SpinnerAdapter sa ;

    ImageView imageView;
    //保存sessionid
    String sessionid = "";
    String ip;
    //记录巡查记录的数量
    int i = 0;


    //正式
    private String bianhao;//编号
    private String xunchajilu;//巡查记录
    private String shifoushangbao;//是否上报
//    private
    private String shangbaoshijian;//上报时间
    private String shijiandidian;//事件地点
    private EditText shangbaodidianE;
    private String JingDu;//经度
    private String WeiDu;//纬度
    private String shangbaobumen;//上报部门
    private EditText shangbaobumenE;
    private String shijianxiaolei;//事件小类
    private EditText shijianxiaoleiE;
    private String shijianshuoming;//事件说明
    private EditText shijianshuomingE;
    private String chuzhiguocheng;//处置过程
    private EditText chuzhiguochengE;
    private String chuzhijieguo;//处置结果
    private EditText chuzhijieguoE;
    private String qunzhongyijian;//群众意见
    private EditText qunzhongyijianE;
    private String genjincuoshi;//跟进措施
    private String beizhu;//备注
    private EditText beizhuE;
    private String tupian;//图片
    private Button submits;
    private Button caogao;
    private Spinner spinner;
    private Spinner spinner2;
    String yuansheng;

    //endregion暂时无关
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_add_event2);

        //region  无关
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
        rs.putValues(new SharedPreferencesUtils.ContentValue("shijiancount"+getTime(new Date()),0));
        String a = ""+  rs.getString("xunchabianhao"+ getTime(new Date()));
        String ssss = "null";

        xunchajilu = rs.getString("xunchabianhao"+getTime(new Date()));
        
        shangbaobumenE = (EditText)findViewById(R.id.shijianleixing);
        shangbaodidianE = (EditText)findViewById(R.id.DiDianE);

        shijianshuomingE = (EditText)findViewById(R.id.ShiJianShuoMingE);
        chuzhiguochengE = (EditText)findViewById(R.id.ChuZhiGuoChengE);
        chuzhijieguoE = (EditText)findViewById(R.id.ChuzhiJieguoE);
        qunzhongyijianE = (EditText)findViewById(R.id.QunzhongyijianE);
        beizhuE = (EditText)findViewById(R.id.BeiZhuE);
//        imageView = (ImageView)findViewById(R.id.picturess);
        submits = (Button)findViewById(R.id.submit);
        caogao = (Button)findViewById(R.id.baocuncaogao);
        if (a.equals(ssss)){
            showToast("今天尚未提交巡查记录！");
            shangbaodidianE.setText("今天尚未提交巡查记录");
            shijianshuomingE.setText("今天尚未提交巡查记录");
            shangbaodidianE.setEnabled(false);
            shijianshuomingE.setEnabled(false);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,shijianleixing);
        final List<ArrayAdapter<String>> adapterList = new ArrayList<ArrayAdapter<String>>();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei3);
        adapterList.add(adapter1);
        adapterList.add(adapter2);
        adapterList.add(adapter3);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner0);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner2.setAdapter(adapterList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //endregion
        //region  无关2
        //地图逻辑
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new AddEvent2Activity.MyLocationListener());
        //添加数据到本地数据库

        MyApplication app =  (MyApplication)getApplication();
        HashMap<String,String>  map = app.getItem();
        final String tag = map.get("id_");

        getTime();
        getTime2();
        getshijiancount();
        requestLocation();


        //region click监听
        final RadioGroup radioGroup  = (RadioGroup) findViewById(R.id.shijianshangbao);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton shangbaobutton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                shangbao01 = shangbaobutton.getText().toString();
            }
        });
        shangbaodidianE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shangbaodidianE.getText().toString().equals("今天尚未提交巡查记录")){
                    Log.d("12345654321", "0000000");
                    showToast("今天尚未提交巡查记录!");
                }
            }
        });
        shijianshuomingE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shijianshuomingE.getText().toString().equals("今天尚未提交巡查记录")){
                    showToast("今天尚未提交巡查记录!");
                }
            }
        });

        submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//                    bianhao = getshijiancount();
                    bianhao = ""+yuansheng;
                    Log.d("12345654321", "数据编号："+bianhao );
                    xunchajilu =""+ rs.getString("xunchabianhao"+getTime(new Date()));
                    Log.d("12345654321", "数据巡查记录："+xunchajilu );
                    shifoushangbao =""+ shangbao01;
                    Log.d("12345654321", "数据是否上报："+shifoushangbao );
                    shangbaoshijian =""+ getTime(new Date());
                    Log.d("12345654321", "数据上报时间："+shangbaoshijian );
                    shijiandidian =""+ shangbaodidianE.getText().toString();
                    Log.d("12345654321", "数据上报地点："+shijiandidian );

                    Log.d("12345654321", "数据经度："+JingDu );

                    Log.d("12345654321", "数据纬度："+WeiDu );

                    Log.d("12345654321", "数据上报部门："+spinner.getSelectedItem().toString() );

                    Log.d("12345654321", "数据小类："+spinner2.getSelectedItem().toString() );
                    shijianshuoming = ""+shijianshuomingE.getText().toString();
                    Log.d("12345654321", "数据事件说明："+shijianshuoming );
                    chuzhiguocheng =""+ chuzhiguochengE.getText().toString();
                    Log.d("12345654321", "数据处置过程："+chuzhiguocheng );
                    chuzhijieguo = ""+chuzhijieguoE.getText().toString();
                    Log.d("12345654321", "数据处置结果："+chuzhijieguo );
                    qunzhongyijian = ""+qunzhongyijianE.getText().toString();
                    Log.d("12345654321", "数据群众意见："+qunzhongyijian );
                    beizhu =""+ beizhuE.getText().toString();
                    Log.d("12345654321", "数据备注："+beizhu );
                }catch (Exception e){
                    Log.d("12345654321", "数据错误："+ e );
                }
                if (shijiandidian.equals("今天尚未提交巡查记录")||shijianshuoming.equals("今天尚未提交巡查记录")||shifoushangbao.equals("")){
                    showToast("今天尚未提交巡查记录！");
                }else if (shijiandidian.equals("")){
                    showToast("事件地点不能为空！");
                }else if (shijianshuoming.equals("")){
                    showToast("事件说明不能为空！");
                }else if (shifoushangbao.equals("")){
                    showToast("未填是否上报！");
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Log.d("12345654321", "进入成功！");
                                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                                OkHttpClient Client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("ShiJianBianHao",bianhao)
                                        .add("XunChaJiLu",xunchajilu)
                                        .add("ShiFouShangBao",shifoushangbao)
                                        .add("ShangBaoShiJian",shangbaoshijian)
                                        .add("ShiJianDiDian",shijiandidian)
                                        .add("JingDu",""+JingDu)
                                        .add("WeiDu",""+WeiDu)
                                        .add("ShangBaoBuMen",""+spinner.getSelectedItem().toString())
                                        .add("ShiJianXiaoLei",""+spinner2.getSelectedItem().toString())
                                        .add("ShiJianShuoMing",shijianshuoming)
                                        .add("ChuZhiGuoCheng",chuzhiguocheng)
                                        .add("ChuLiJieGuo",chuzhijieguo)
                                        .add("QunZhongYiJian",qunzhongyijian)
                                        .add("GenJinCuoShi","")
                                        .add("BeiZhu",beizhu)
                                        .build();
                                Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                                        .url("http://"+ip+"/MainWangGe/shijianxinxi_creat").post(requestBody).build();
                                Log.d("12345654321", "我准备好了！" );
                                Response response = Client.newCall(request).execute();
                                String con = response.body().string();
                                Log.d("12345654321", "我获取到了！" + con.trim().substring(0,4) );
                                if (con.equals('"'+"Yes"+'"')){
                                    showToast("事件提交成功！");
                                    xunchajilu = "null";
                                    rs.putValues(new SharedPreferencesUtils.ContentValue("shijiancount"+getTime(new Date()),rs.getInt("shijiancount"+getTime(new Date()))+1),
                                            new SharedPreferencesUtils.ContentValue("xunchabianhao"+ getTime(new Date()),xunchajilu));
                                    int q = imgList.size();
                                    if (!imgList.isEmpty()){
                                        for (int i = 0;i<q;i++){
                                            try{
                                                OkHttpClient okHttpClient = new OkHttpClient();
                                                File file1 = addfile(imgList.get(i));
                                                RequestBody fileBody1 = RequestBody.create(MediaType.parse("image"),file1);
                                                RequestBody requestBody1 = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE)
                                                        .addFormDataPart("ShiJianBianHao",bianhao)
                                                        .addPart(fileBody1)
                                                        .build();
                                                Request requestimg = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                                                        .url("http://"+getResources().getString(R.string.httpclient)+"/ashx/HandleImg.ashx").post(requestBody1).build();
                                                Response responseimg = okHttpClient.newCall(requestimg).execute();
                                                Log.d("12345654321", "guocheng7");
                                                String img = responseimg.body().string();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        showToast("图片上传成功！");
                                    }
//                                    showToast("提交成功！");
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(AddEvent2Activity.this,Main2Activity.class);
                                    startActivity(intent);
                                }else if (con.trim().substring(0,5).equals("<!DOC")){
                                    showToast("重复提交！");
                                } else{
                                    showToast("提交失败");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.d("12345654321", "出错了！->"+ e );
                            }
                        }
                    }).start();
                }

            }
        });
        caogao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//                    bianhao = getshijiancount();
                    bianhao =""+ yuansheng;
                    Log.d("12345654321", "数据编号："+bianhao );
                    xunchajilu =""+ rs.getString("xunchabianhao"+getTime(new Date()));
                    Log.d("12345654321", "数据巡查记录："+xunchajilu );
                    shifoushangbao =""+ shangbao01;
                    Log.d("12345654321", "数据是否上报："+shifoushangbao );
                    shangbaoshijian =""+ getTime(new Date());
                    Log.d("12345654321", "数据上报时间："+shangbaoshijian );
                    shijiandidian =""+ shangbaodidianE.getText().toString();
                    Log.d("12345654321", "数据上报地点："+shijiandidian );

                    Log.d("12345654321", "数据经度："+JingDu );

                    Log.d("12345654321", "数据纬度："+WeiDu );

                    Log.d("12345654321", "数据上报部门："+spinner.getSelectedItem().toString() );

                    Log.d("12345654321", "数据小类："+spinner2.getSelectedItem().toString() );
                    shijianshuoming =""+ shijianshuomingE.getText().toString();
                    Log.d("12345654321", "数据事件说明："+shijianshuoming );
                    chuzhiguocheng =""+ chuzhiguochengE.getText().toString();
                    Log.d("12345654321", "数据处置过程："+chuzhiguocheng );
                    chuzhijieguo = ""+chuzhijieguoE.getText().toString();
                    Log.d("12345654321", "数据处置结果："+chuzhijieguo );
                    qunzhongyijian =""+ qunzhongyijianE.getText().toString();
                    Log.d("12345654321", "数据群众意见："+qunzhongyijian );
                    beizhu =""+ beizhuE.getText().toString();
                    Log.d("12345654321", "数据备注："+beizhu );
                }catch (Exception e){
                    Log.d("12345654321", "数据错误："+ e );
                }
                if (shijiandidian.equals("")||shijianshuoming.equals("")||shifoushangbao.equals("")){
                    showToast("有必填项未填！");
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                                OkHttpClient Client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("ShiJianBianHao",bianhao)
                                        .add("XunChaJiLu",xunchajilu)
                                        .add("ShiFouShangBao",shifoushangbao)
                                        .add("ShangBaoShiJian",shangbaoshijian)
                                        .add("ShiJianDiDian",shijiandidian)
                                        .add("JingDu",""+JingDu)
                                        .add("WeiDu",""+WeiDu)
                                        .add("ShangBaoBuMen",""+spinner.getSelectedItem().toString())
                                        .add("ShiJianXiaoLei",""+spinner2.getSelectedItem().toString())
                                        .add("ShiJianShuoMing",shijianshuoming)
                                        .add("ChuZhiGuoCheng",chuzhiguocheng)
                                        .add("ChuLiJieGuo",chuzhijieguo)
                                        .add("QunZhongYiJian",qunzhongyijian)
                                        .add("GenJinCuoShi","")
                                        .add("BeiZhu",beizhu)
                                        .build();
                                Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                                        .url("http://"+ip+"/MainWangGe/baocuncaogao").post(requestBody).build();
                                Log.d("12345654321", "我准备好了！" );
                                Response response = Client.newCall(request).execute();
                                String con = response.body().string();
                                Log.d("12345654321", "我获取到了！" + con );
                                if (con.equals("Yes")){
                                    showToast("提交成功！");
                                    rs.putValues(new SharedPreferencesUtils.ContentValue("shijiancount"+getTime(new Date()),rs.getInt("shijiancount"+getTime(new Date()))+1));
                                    rs.putValues(new SharedPreferencesUtils.ContentValue("0",bianhao),
                                            new SharedPreferencesUtils.ContentValue("1",xunchajilu),
                                            new SharedPreferencesUtils.ContentValue("2",shifoushangbao),
                                            new SharedPreferencesUtils.ContentValue("3",shangbaoshijian),
                                            new SharedPreferencesUtils.ContentValue("4",shijiandidian),
                                            new SharedPreferencesUtils.ContentValue("5",JingDu),
                                            new SharedPreferencesUtils.ContentValue("6",WeiDu),
                                            new SharedPreferencesUtils.ContentValue("7",spinner.getSelectedItem().toString()),
                                            new SharedPreferencesUtils.ContentValue("8",spinner2.getSelectedItem().toString()),
                                            new SharedPreferencesUtils.ContentValue("9",shijianshuoming),
                                            new SharedPreferencesUtils.ContentValue("10",chuzhiguocheng),
                                            new SharedPreferencesUtils.ContentValue("11",chuzhijieguo),
                                            new SharedPreferencesUtils.ContentValue("12",qunzhongyijian),
                                            new SharedPreferencesUtils.ContentValue("13",""),
                                            new SharedPreferencesUtils.ContentValue("14",beizhu),
                                            new SharedPreferencesUtils.ContentValue("15",imgList));
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(AddEvent2Activity.this,Main2Activity.class);
                                    startActivity(intent);
                                }if (con.trim().substring(0,5).equals("<!DOC")){
                                    showToast("重复提交！");
                                }else {
                                    showToast("提交失败");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.d("12345654321", "出错了！->"+ e );
                            }
                        }
                    }).start();
                }

            }
        });

        //endregion
//        init();
//        initData();

        mContext = this;
        ivDelete = (ImageView) findViewById(R.id.img_delete);
        gridView = (GridView) this.findViewById(R.id.gridView);
        myAdapter = new MyAdapter();
        myAdapter.setIsShowDelete(isShowDelete);
        gridView.setAdapter(myAdapter);


        //region 事件监听
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ivDelete = (ImageView) findViewById(R.id.img_delete);
                if (isShowDelete == true) {
                    // 如果处于正在删除的状态，单击则删除图标消失
                    isShowDelete = false;
                    myAdapter.setIsShowDelete(isShowDelete);
                } else {
                    // 处于正常状态
                    if (arg2 > imgList.size() - 1) {
                        // 添加图片
//                        selectimg();
                        toGetCameraImage();
                    } else {
                        Context context = AddEvent2Activity.this;
                        final Dialog dialog = new Dialog(context,R.style.mydialogss);
                        dialog.setContentView(R.layout.start_dialog);
                        ImageView imageView = (ImageView)dialog.findViewById(R.id.pictruedialog);
                        imageView.setBackgroundColor(getResources().getColor(R.color.black));
                        imageView.setImageBitmap(imgList.get(arg2));
                        dialog.setCanceledOnTouchOutside(true);
                        Window w = dialog.getWindow();
                        WindowManager.LayoutParams lp = w.getAttributes();
                        lp.x = 0;
                        lp.y = 40;
                        dialog.onWindowAttributesChanged(lp);
                        imageView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });

        // 长按事件
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                // 长按显示删除图标
                if (isShowDelete == false) {
                    isShowDelete = true;
                }
                myAdapter.setIsShowDelete(isShowDelete);
                return true;
            }
        });


        //endregion
    }


    private File addfile(Bitmap bmp){
        int x = (int)(1+Math.random()*(100-1+1));
        File file = new File(getCacheDir(), ""+x+".png");
        try{
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.PNG,100,bos);
            bos.flush();
            bos.close();
        }catch (Exception e){
            e.printStackTrace();
            file = null;
        }
        return file;
    }
    private File addtofile(List<Bitmap> list){

        File file = new File(getExternalCacheDir(),"temp.png");
        if (!file.exists()){
            file.getParentFile().mkdir();
            try{
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        OutputStream os = null;
        Log.d("12345654321", "zheli1");
        ByteArrayOutputStream baoss = new ByteArrayOutputStream();
        int size = imgList.size();
        Log.d("12345654321", "zheli0："+ size);
        try{
            for (int i=0;i<size;i++){
                Log.d("12345654321", "zheli2："+ i);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                list.get(i).compress(Bitmap.CompressFormat.PNG,100,baos);
                Log.d("12345654321", "zheli3");
                baoss.write(baos.toByteArray());
                Log.d("12345654321", "zheli4");
            }
            InputStream ins = new ByteArrayInputStream(baoss.toByteArray());
            FileOutputStream outputStream=new FileOutputStream(new File(""));

            Log.d("12345654321", "zheli5");
            int bytesRead = 0;
//            os = new FileOutputStream(file,false);
//            while ((bytesRead = ins.read())>0){
//                os.write(bytesRead);
//            }
            byte[] bs = baoss.toByteArray();
            Log.d("12345654321", "zheli6");
            byte[] buffer = new byte[8192];
            Log.d("12345654321", "zheli7");
//            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
            os.write(bs);
//            while ((bytesRead = ins.read(buffer))>0){
//                os.write(buffer,0,bytesRead);
//            }
            Log.d("12345654321", "zheli8");
            os.close();
            ins.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "出错：" + e );
            return file;
        }
    }

    private String BitmapToStream(Bitmap bitmap){
        OutputStream outputStream = null;
        try{
            bitmap.compress(Bitmap.CompressFormat.PNG,30,outputStream);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
        return outputStream.toString();
    }
    /**
     * 选择图片来源
     */
    private void selectimg() {
        final CharSequence[] items = { "拍照上传", "从相册选择" };
        new AlertDialog.Builder(this).setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toGetCameraImage();
//                        if (which == SELECT_PICTURE) {
//                            toGetLocalImage();
//                        } else {
//                            toGetCameraImage();
//                        }
                    }
                }).create().show();
    }
    /**
     * 选择本地图片
     */
    public void toGetLocalImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);

    }
    /**
     * 照相选择图片
     */
    public void toGetCameraImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        String photoname = ""+(System.currentTimeMillis()/60);
        out = new File(getSDPath(), photoname);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, SELECT_CAMER);
        // finish();
    }
    /**
     * 获取sd卡路径
     *
     * @return
     */
    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory()
                    + "/DCIM/Camera");

        }
        return sdDir;
    }

    //region  暂时无关方法
    //获得事件编号
    private void  getshijiancount(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    String bian = getTime2(new Date());
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("ShiJianBianHao",bian)
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("http://"+ip+"/MainWangGe/get_ShiJianBianHao")
                            .post(requestBody)
                            .build();
                    Log.d("12345654321", "我准备好了！" );
                    Response response = Client.newCall(request).execute();
                    yuansheng = response.body().string();
                    Log.d("12345654321", "我获取到了："+yuansheng );
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("12345654321", "出错了！->"+ e );
                }
            }
        }).start();
    }
    private String getTime(Date date){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(date);
        return ee;
    }
    private String getTime2(Date date){
        SimpleDateFormat dff = new SimpleDateFormat("yyyyMMdd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(date);
        return ee;
    }
    //发送事件请求
    private void sengMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
//                            .add("BianHao",bianhaoget)
//                            .add("XunChaRiQi",XunChaRiQi.getText().toString())
//                            .add("SSWangGe",SSWangGe.getText().toString())
//                            .add("XunChaRenYuan",WGYxingming.getText().toString())
//                            .add("XunChaQuYu",Xunchaquyu.getText().toString())
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("http://192.168.1.236:8010/MainWangGe/shijianxinxi_creat").post(requestBody).build();
                    Log.d("12345654321", "我又准备好了！" );
                    Response response = Client.newCall(request).execute();
                    String con = response.body().string();
                    Log.d("12345654321", "我又获取到了！" );
                }catch (Exception e){
                    Log.d("12345654321", "出错：" + e );
                }
            }
        }).start();
    }
    //巡查记录数量
    private int xunchacount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().build();
                    Request request = new Request.Builder().addHeader("Cookie", "ASP.NET_SessionId=" + sessionid)
                            .url("http://192.168.1.236:8010/MainWangGe/get_messagecount_xunchajilu")
                            .post(requestBody)
                            .build();
                    Response response = Client.newCall(request).execute();
                    String yuansheng = response.body().string();
                    i = Integer.parseInt(yuansheng);
                } catch (Exception e) {
                    Log.d("12345654321", "出错："+ e );
                }
            }
        }).start();
        return i;
    }
    //获取所有事件的数目
    private Integer getCount(){
        int count =0;
        SQLiteDatabase db = datebaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from ShiJianXinXi",new String []{});
        while (cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        return count;
    }
    @Override
    protected void onDestroy() {
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }

    //获取经纬度方法
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

    //获取并将经纬度填入
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            currentPosition2.append(location.getLatitude());
            currentPosition.append(location.getLongitude());
            WeiDu= currentPosition2.toString();
            JingDu = currentPosition.toString();
            Log.d("12345654321", "zhixing"+JingDu );
        }
    }

    //endregion


    //region  暂时不要
//    private void init() {
//        gridView = (MyGridView) this.findViewById(R.id.gridView);
//        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        gridView.setOnItemClickListener(this);
//        dialog = new MyDialog(this);
//        dialog.setOnButtonClickListener(this);
//        // activity中调用其他activity中组件的方法
//        LayoutInflater layout = this.getLayoutInflater();
//        View view = layout.inflate(R.layout.layout_select_photo, null);
//    }
//    private void initData() {
//        /*
//         * 载入默认图片添加图片加号
//         */
//        bmp = BitmapFactory.decodeResource(getResources(),
//                R.drawable.gridview_addpic); // 加号
//        imageItem = new ArrayList<HashMap<String, Object>>();
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("itemImage", bmp);
//        imageItem.add(map);
//        simpleAdapter = new SimpleAdapter(this, imageItem,
//                R.layout.griditem_addpic, new String[] { "itemImage" },
//                new int[] { R.id.imageView1 });
//        simpleAdapter.setViewBinder(new ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data,
//                                        String textRepresentation) {
//                // TODO Auto-generated method stub
//                if (view instanceof ImageView && data instanceof Bitmap) {
//                    ImageView i = (ImageView) view;
//                    i.setImageBitmap((Bitmap) data);
//                    return true;
//                }
//                return false;
//            }
//        });
//        gridView.setAdapter(simpleAdapter);
//    }
//    @Override
//    public void camera() {
//        // TODO Auto-generated method stub

//        File outputimage = new File(getExternalCacheDir(),"output_image.jpg");
//        try{
//            if (outputimage.exists()){
//                outputimage.delete();
//            }
//            outputimage.createNewFile();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if (Build.VERSION.SDK_INT>24){
//            image = FileProvider.getUriForFile(AddEvent2Activity.this,"com.alex.demo.FileProvider",outputimage);
//        }else {
//            image = Uri.fromFile(outputimage);
//        }
//        Intent intents = new Intent("android.media.action.IMAGE_CAPTURE");
//        intents.putExtra(MediaStore.EXTRA_OUTPUT,image);
//        startActivityForResult(intents,1);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//                Environment.getExternalStorageDirectory(), "temp.jpg")));
//        startActivityForResult(intent, PHOTOHRAPH);
//    }

//    @Override
//    public void gallery() {
//        // TODO Auto-generated method stub
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, IMAGE_OPEN);
//
//    }
//
//    @Override
//    public void cancel() {
//        // TODO Auto-generated method stub
//        dialog.cancel();
//    }
//endregion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case SELECT_PICTURE:
                    Uri vUri = data.getData();
                    // 将图片内容解析成字节数组
                    String[] proj = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = managedQuery(vUri, proj, null, null, null);//过时了......
                    Cursor cursor = mContext.getContentResolver().query(vUri,proj,null,null,null);
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path1 = cursor.getString(column_index);
                    Bitmap bm = Until.getxtsldraw(AddEvent2Activity.this, path1);
                    path = Until.creatfile(mContext, bm, ""+(System.currentTimeMillis()));
                    if (null != bm && !"".equals(bm)) {
                        imgList.add(bm);
                    }
                    myAdapter.notifyDataSetChanged();
                    break;
                //拍照添加图片
                case SELECT_CAMER:
                    Bitmap bm1 = Until.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Until.creatfile(mContext, bm1, ""+(System.currentTimeMillis()));

                    if (null != bm1 && !"".equals(bm1)) {
                        imgList.add(bm1);
                    }
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

//region  暂时不要

//        if (resultCode == NONE)
//            return;
//        // 拍照
//        if (requestCode == PHOTOHRAPH) {
//            // 设置文件保存路径这里放在跟目录下
//            File picture = new File(Environment.getExternalStorageDirectory()
//                    + "/temp.jpg");
//            startPhotoZoom(Uri.fromFile(picture));
//        }
//        if (data == null)
//            return;
//        // 处理结果
//        if (requestCode == PHOTORESOULT) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
//                // 将图片放入gridview中
//                HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put("itemImage", photo);
//                imageItem.add(map);
//                simpleAdapter = new SimpleAdapter(this, imageItem,
//                        R.layout.griditem_addpic, new String[] { "itemImage" },
//                        new int[] { R.id.imageView1 });
//                simpleAdapter.setViewBinder(new ViewBinder() {
//                    @Override
//                    public boolean setViewValue(View view, Object data,
//                                                String textRepresentation) {
//                        // TODO Auto-generated method stub
//                        if (view instanceof ImageView && data instanceof Bitmap) {
//                            ImageView i = (ImageView) view;
//                            i.setImageBitmap((Bitmap) data);
//                            imageView.setImageBitmap((Bitmap)data);
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//                gridView.setAdapter(simpleAdapter);
//                simpleAdapter.notifyDataSetChanged();
//                dialog.dismiss();
//            }
//        }
//        // 打开图片
//        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
//            startPhotoZoom(data.getData());
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//endregoin

    }
    //region buyao
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        if (!TextUtils.isEmpty(pathImage)) {
//            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("itemImage", addbmp);
//            imageItem.add(map);
//            simpleAdapter = new SimpleAdapter(this, imageItem,
//                    R.layout.griditem_addpic, new String[] { "itemImage" },
//                    new int[] { R.id.imageView1 });
//            simpleAdapter.setViewBinder(new ViewBinder() {
//                @Override
//                public boolean setViewValue(View view, Object data,
//                                            String textRepresentation) {
//                    // TODO Auto-generated method stub
//                    if (view instanceof ImageView && data instanceof Bitmap) {
//                        ImageView i = (ImageView) view;
//                        i.setImageBitmap((Bitmap) data);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            gridView.setAdapter(simpleAdapter);
//            simpleAdapter.notifyDataSetChanged();
//            // 刷新后释放防止手机休眠后自动添加
//            pathImage = null;
//            dialog.dismiss();
//        }
//
//    }
    //endregion

    //region  暂时不要

//    @Override
//    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//        // TODO Auto-generated method stub
//        if (imageItem.size() == 10) { // 第一张为默认图片
//            Toast.makeText(AddEvent2Activity.this, "图片数9张已满",
//                    Toast.LENGTH_SHORT).show();
//        } else if (position == 0) { // 点击图片位置为+ 0对应0张图片
//            // 选择图片
//            dialog.show();
//            // 通过onResume()刷新数据
//        } else {
//            dialog(position);
//        }
//
//    }

    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
//    protected void dialog(final int position) {
//        AlertDialog.Builder builder = new Builder(AddEvent2Activity.this);
//        builder.setMessage("确认移除已添加图片吗？");
//        builder.setTitle("提示");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                imageItem.remove(position);
//                simpleAdapter.notifyDataSetChanged();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }

//    public void startPhotoZoom(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 64);
//        intent.putExtra("outputY", 64);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PHOTORESOULT);
//    }

    //endregion
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddEvent2Activity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgList.size() + 1;
        }

        @Override
        public Object getItem(int arg0) {
            return imgList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            //初始化页面和相关控件
            convertView = inflater.inflate(R.layout.item_imgview, null);
            ImageView img_pic = (ImageView) convertView
                    .findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView
                    .findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView
                    .findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgList.size() >= 1) {
                if (position <= imgList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgList.get(position));
                    // 设置删除按钮是否显示
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }

            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imgList.remove(position);
                        myAdapter.notifyDataSetChanged();

                    }
                });
            }
            return convertView;
        }

        /**
         * 设置是否显示删除图片
         *
         * @param isShowDelete
         */
        public void setIsShowDelete(boolean isShowDelete) {
            this.isDelete = isShowDelete;
            notifyDataSetChanged();
        }

    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}


