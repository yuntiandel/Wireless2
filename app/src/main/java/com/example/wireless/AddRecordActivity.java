package com.example.wireless;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.jtds.jdbc.DateTime;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import DataBaseUtil.MyDatebaseHelper;
import bean.RecordData;
import bean.ShiJianXinXi;
import myapp.MyApplication;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import util.SharedPreferencesUtils;

public class AddRecordActivity extends AppCompatActivity {
    //编号
    private String BianHao;
    private int x = 10;
    //巡查日期
    private EditText XunChaRiQi;
    private String datetime;
    //所属网格
    private EditText SSWangGe;
    private String SSWG;
    //网格员姓名
    private EditText WGYxingming;
    private String WGY;
    //巡查区域
    private EditText Xunchaquyu;
    private String XCQY;
    String sessionid = "";
    String ip;
    String bianhaoget;
    String renyuanget;
    String wanggeget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
        XunChaRiQi = (EditText) findViewById(R.id.XunChaShiJian);
        Xunchaquyu = (EditText) findViewById(R.id.XunChaQuYu);
        WGYxingming = (EditText) findViewById(R.id.WGYXinGMing);
        SSWangGe = (EditText)findViewById(R.id.SS_WangGe);
        Button submit = (Button) findViewById(R.id.recordsubmit);
        getxiangguanxinxi();
        getxunchaTime();
        Log.d("12345654321", "投影完毕！" );
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.recordsubmit){
                    sendrecord();
                    rs.putValues(new SharedPreferencesUtils.ContentValue("xunchabianhao"+ getTime(new Date()),""+ bianhaoget));
                }
            }
        });
    }
    private String getTime(Date date){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(date);
        return ee;
    }
    //获取巡查时间的方法
    private void getxunchaTime(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd ");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        CharSequence cs = null;
        cs = ee;
        datetime = cs.toString();
        XunChaRiQi.setText(datetime);
        Log.d("12345654321", "时间数据投影完毕！");
    }
    private String Bianhao(){
        SimpleDateFormat fff = new SimpleDateFormat("yyyyMMdd");
        fff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String x = fff.format(new Date());
        return x;
    }
    //填写完信息，发送请求，提交巡查记录
    private void sendrecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("BianHao",bianhaoget)
                            .add("XunChaRiQi",XunChaRiQi.getText().toString())
                            .add("SSWangGe",SSWangGe.getText().toString())
                            .add("XunChaRenYuan",WGYxingming.getText().toString())
                            .add("XunChaQuYu",Xunchaquyu.getText().toString())
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("http://"+ip+"/MainWangGe/MWGbottomlist").post(requestBody).build();
                    Log.d("12345654321", "我又准备好了！" );
                    Response response = Client.newCall(request).execute();
                    String con = response.body().string();
                    Log.d("12345654321", "我又获取到了！" );
                    if (con.equals('"'+"Yes"+'"')){
                        showToast("提交成功！");
                        Thread.sleep(1000);
                        Intent intent = new Intent(AddRecordActivity.this,Main2Activity.class);
                        startActivity(intent);
                    }else {
                        showToast("提交失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //获取对应信息
    private void getxiangguanxinxi(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    String bian = Bianhao();
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("XunChaBianHao",bian)
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("http://"+ip+"/MainWangGe/get_XunChaXinXi")
                            .post(requestBody)
                            .build();
                    Log.d("12345654321", "我准备好了！" );
                    Response response = Client.newCall(request).execute();
                    String yuansheng = response.body().string();
                    Log.d("12345654321", "我获取到了："+yuansheng );
                    Gson gson = new Gson();
                    String realjson = yuansheng.substring(1,yuansheng.length()-1);
                    Log.d("12345654321", "数据："+realjson );
                    java.lang.reflect.Type type = new TypeToken<RecordData>(){}.getType();
                    RecordData re  = gson.fromJson(realjson,type);
                    bianhaoget = re.getXunChaBianHao();
                    renyuanget = re.getXunChaRenYuan();
                    wanggeget = re.getSSWangGe();
                    Log.d("12345654321", "数据："+bianhaoget );
                    Log.d("12345654321", "数据："+renyuanget);
                    Log.d("12345654321", "数据："+wanggeget );
                    SSWangGe.setText(wanggeget);
                    WGYxingming.setText(renyuanget);
                    Log.d("12345654321", "网格及人员数据投影完毕！");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("12345654321", "出错了！->"+ e );
                }
            }
        }).start();
    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddRecordActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}