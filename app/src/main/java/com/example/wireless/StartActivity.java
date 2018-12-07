package com.example.wireless;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import DataBaseUtil.MyDatebaseHelper;
import bean.LoginGet;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.Base64Utils;
import util.SharedPreferencesUtils;


public class StartActivity extends AppCompatActivity {

    private RollPagerView mRollViewPager;
   // private RollPagerView mRollViewPager2;
   String data = "No";
    String sessionid = "";
    String name;
    String ip;
    String exception = "null";
    public static final String Create_ShiJianXinXi ="create table ShiJianXinXi("+"ShiJianDaiMa TEXT (20) NOT NULL,"+"ShiJianShuoMing TEXT (30),"+"DiDian TEXT (20),"+"JingDu TEXT (10),"+"WeiDu TEXT (10),"+"ShangBaoShiJian TEXT (14),"+"TuPian TEXT (50),"+"BeiZhu TEXT (200),"+"ShiFouShangBao TEXT (4),"+"ShiJianLeiXing TEXT (20),"+"ChuZhiGuoCheng TEXT (200),"+"ChuZhiJieGuo TEXT (200))";
    public static final String Create_Login ="create table Login("+"id_ TEXT (12) PRIMARY KEY UNIQUE NOT NULL,"+"username TEXT (20) UNIQUE NOT NULL,"+"password TEXT (20) NOT NULL,"+"flag     TEXT (2)  NOT NULL)";
    private MyDatebaseHelper myDatebaseHelper;
    public static final String XunChaJiLu = "create table XunChaJiLu(";
    public static final String RENYUANXINXI =        "create table Renyuanxinxi(" + "Username text NOT NULL , " + "Userpwd text NOT NULL ," + "Xingming text," + "Zhize text," + "Xingbie text," + "Dianhua text," + "BianHao integer primary key not null," + "Tag integer," + "WangGeZhangXingming text," + "SuoShuWangGe text," + "SuoShuGongZuoZhan text )";
    private final int CHANGEUI = 0;
    /*
    * 判断用户是否为第一次登陆，如果是，则在开始界面创建数据库
    *
    * */
    Button button;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGEUI:
                    changeui();
                    break;
                default:
                    break;
            }
        }
    };

    private void changeui(){
        button.setVisibility(View.VISIBLE);
    }
//    public void time(){
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        };
//        timer.schedule(timerTask,3000);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        button = (Button)findViewById(R.id.startbutton);
        button.setVisibility(View.GONE);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(StartActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(StartActivity.this, permissions, 1);
        } else {

        }

//        try{
//            ip = getIntent().getStringExtra("ipconfigs");
//            Log.d("12345654321", "Start获得ip:" + ip );
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        ip = getResources().getString(R.string.httpclients);
        boolean rempsd = remenberPassword();
        if (firstLogin()||!rempsd)
        {
            myDatebaseHelper = new MyDatebaseHelper(this,"WangGe.db",null,1);
            SQLiteDatabase database = myDatebaseHelper.getReadableDatabase();
            database.close();
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message message2 = new Message();
                    message2.what = CHANGEUI;
                    handler.sendMessage(message2); //执行
                }
            };
            timer.schedule(task, 1000 * 3); //3秒后
        }else {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    zidong();
                }
            };
            timer.schedule(task, 1000 * 3); //3秒后

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils rs = new SharedPreferencesUtils(StartActivity.this,"setting");
//                rs.putValues(new SharedPreferencesUtils.ContentValue("ipconfig",ip));
                admin();
            }
        });
    }

    private boolean remenberPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean remenberPassword = helper.getBoolean("remenberPassword", false);
        return remenberPassword;
    }
    private void admin(){
        Intent intent = new Intent();
        intent.setClass(StartActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        boolean first = helper.getBoolean("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("name", ""),
                    new SharedPreferencesUtils.ContentValue("password", ""));
//                    new SharedPreferencesUtils.ContentValue("ipconfig",ip)


            return true;
        }
        return false;
    }

    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String name = helper.getString("nameacc");
        return name;
    }
    /**
     * 获得保存在本地的密码
     */
    public String getLocalPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String password = helper.getString("password");
        return Base64Utils.decryptBASE64(password);   //解码一下
//       return password;   //解码一下

    }
    private void zidong(){
        String user = "" + getLocalName();
        String pass = "" + getLocalPassword();
        //创建请求信息
        final OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userName", user)
                .add("passWord", pass)
                .add("Index", "0")
                .build();
//        SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
//        String ip = rs.getString("ipconfig");
        final Request request = new Request.Builder()
                .url("http://"+ip+"/Login/Login")
                .post(requestBody)
                .build();
        final SharedPreferencesUtils s = new SharedPreferencesUtils(this,"setting");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    Response response  = okHttpClient.newCall(request).execute();
                    data = response.body().string();
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<LoginGet>(){}.getType();
                    LoginGet re  = gson.fromJson(data,type);
                    sessionid = re.getSessionID().toString();
                    name = re.getName().toString();
                }catch (Exception e){
                    e.printStackTrace();
                    exception = e.toString();
                    Log.d("12345654321", "run: "+ e);
                }
                //判断账号和密码
                if (!exception.equals("null")||data.equals("No")){
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }else {
                    //保存用户的SessionId和网格员姓名
                    s.putValues(new SharedPreferencesUtils.ContentValue("sessionid",sessionid));
                    s.putValues(new SharedPreferencesUtils.ContentValue("name",name));
//                    s.putValues(new SharedPreferencesUtils.ContentValue("ipconfig",ip));
                    //登录成功，跳转主页
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }
            }
        }).start();
    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



