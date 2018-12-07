package com.example.wireless;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBaseUtil.MyDatebaseHelper;
import bean.ShiJianXinXi;
import bean.ShiJianXinXiGet;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.SharedPreferencesUtils;

public class DoubleActivity extends AppCompatActivity {
    List<String> list = new ArrayList<String>();
    List<ImageView> listpic = new ArrayList<>();
    public String data;
    public String BianHao;
    String jingdu;
    String weidu;
    private WebView webView;
    Bitmap bitmap;
    final private int GETPIC = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETPIC:
                    getPic();
                    break;
                default:
                    break;
            }
        }
    };
    @JavascriptInterface
    public String getjingdu(){
        return jingdu;
    }
    @JavascriptInterface
    public String getweidu(){
        return weidu;
    }
    ShiJianXinXiGet shiJianXinXiGet;
    String sessionid;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double);
        Intent intent = new Intent();
        final SharedPreferencesUtils rs= new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
//        String[] xx =  intent.getStringArrayExtra("");
        shiJianXinXiGet = (ShiJianXinXiGet)getIntent().getSerializableExtra("shijianxinxi");
        try{
            jingdu = shiJianXinXiGet.getJingDu();
            weidu = shiJianXinXiGet.getWeiDu();
            String key = shiJianXinXiGet.getTuPian();
            bitmap = keytomap(key);
        }catch (Exception e){
            Log.d("12345654321", "出错: " + e );
        }
        rs.putValues(new SharedPreferencesUtils.ContentValue("jingdu",jingdu),new SharedPreferencesUtils.ContentValue("weidu",weidu));
        //加载地图
        start222();
        ImageView s1 = (ImageView)findViewById(R.id.first);
        ImageView s2 = (ImageView)findViewById(R.id.second);
        ImageView s3 = (ImageView)findViewById(R.id.third);
        ImageView s4 = (ImageView)findViewById(R.id.forth);
        ImageView s5 = (ImageView)findViewById(R.id.fifth);
        ImageView s6 = (ImageView)findViewById(R.id.sixth);
        ImageView s7 = (ImageView)findViewById(R.id.seventh);
        ImageView s8 = (ImageView)findViewById(R.id.eighth);
        ImageView s9 = (ImageView)findViewById(R.id.ninth);
        listpic.add(s1);
        listpic.add(s2);
        listpic.add(s3);
        listpic.add(s4);
        listpic.add(s5);
        listpic.add(s6);
        listpic.add(s7);
        listpic.add(s8);
        listpic.add(s9);
        TextView time = (TextView)findViewById(R.id.timedou);
        TextView place = (TextView)findViewById(R.id.didiandou);
        TextView shuoming = (TextView)findViewById(R.id.shuomingdou);
        TextView department = (TextView)findViewById(R.id.deptmentdou);
        TextView xiaolei = (TextView)findViewById(R.id.shijianxiaoleidou);
        TextView zhunagtai = (TextView)findViewById(R.id.chuzhizhuangtaidou);
        TextView jieguo = (TextView)findViewById(R.id.chuzhijieguodou);
        TextView popyijian = (TextView)findViewById(R.id.qunzhongyijiandou);
        TextView genjin = (TextView)findViewById(R.id.genjincuoshidou);
        TextView beizhu = (TextView)findViewById(R.id.beizhudou);
        try{
            time.append(changetime(shiJianXinXiGet.getShangBaoShiJian()).trim());
            place.append(shiJianXinXiGet.getShiJianDiDian().trim());
            shuoming.append(shiJianXinXiGet.getShiJianShuoMing().trim());
            department.append(shiJianXinXiGet.getShangBaoBuMen().trim());
            xiaolei.append(shiJianXinXiGet.getShiJianXiaoLei().trim());
            zhunagtai.append(shiJianXinXiGet.getChuZhiGuoCheng().trim());
            jieguo.append(shiJianXinXiGet.getChuLiJieGuo().trim());
            popyijian.append(shiJianXinXiGet.getQunZhongYiJian().trim());
            genjin.append(shiJianXinXiGet.getGenJinCuoShi().trim());
            beizhu.append(shiJianXinXiGet.getBeiZhu().trim());
        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "出错: " + e );
        }
        Message message = new Message();
        message.what = GETPIC;
        handler.sendMessage(message);
    }
    private String changetime(String time){
        try{
            Log.d("12345654321", "时间格式time:" + time);
            SimpleDateFormat fff = new SimpleDateFormat("yyyy-MM-dd");
            String ss = time.substring(6,time.length()-2);
            Log.d("12345654321", "时间格式ss:" + ss);
            long sa = Long.parseLong(ss);
            String x = fff.format(sa);
            Log.d("12345654321", "时间格式x:" + x);
            return x;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "出错：" + e);
            return "未知时间";
        }
    }
    //加载地图
    private void start222(){
        webView = (WebView) findViewById(R.id.web_view2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new DoubleActivity(),"js3");
        webView.addJavascriptInterface(new JsInterface(this),"AndroidToJs");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(dir);

        webView.loadUrl("file:///android_res/raw/index2.html");
    }


    //String字符串转换成bitmap图片
    private Bitmap keytomap(String key){
        Bitmap bitmap = null;
        try{
            byte[] bytes = Base64.decode(key,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }catch (Exception e){
            return  null;
        }
    }
    class JsInterface{
        private Context contexts;
        public JsInterface(Context context){
            this.contexts = context;
        }
        @JavascriptInterface
        public String jingdu(){
            SharedPreferencesUtils ss = new SharedPreferencesUtils(this.contexts,"setting");
            String aaa = ss.getString("jingdu");
            return aaa;
        }
        @JavascriptInterface
        public String weidu(){
            SharedPreferencesUtils ss = new SharedPreferencesUtils(this.contexts,"setting");
            String aaa = ss.getString("weidu");
            return aaa;
        }
    }

     private void getPic(){
        try{
            OkHttpClient pic = new OkHttpClient();
            RequestBody rb = new FormBody.Builder().add("ShiJianBianHao",shiJianXinXiGet.getShiJianBianHao().toString()).build();
            Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                    .url("http://"+ip+"/ashx/HandleImg.ashx").post(rb).build();
            Response rsp = pic.newCall(request).execute();
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<String>(){}.getType();
            String sss = rsp.body().string();
            if (!sss.equals("[]")){
                list = gson.fromJson(sss.substring(1,sss.length()-1),type);
                int as = list.size();
                for (int i = 0;i<as;i++){
                    Bitmap bitmap = getpicture(i);
                    listpic.get(i).setImageBitmap(bitmap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Bitmap getpicture(int a){
        String ss = shiJianXinXiGet.getShiJianBianHao().toString();
        Bitmap bitmap = null;
        try{
            OkHttpClient pic = new OkHttpClient();
            RequestBody rb = new FormBody.Builder().add("ShiJianBianHao",ss).build();
            Request request = new Request.Builder()
                    .url("http://192.168.1.236:8010/Image/"+list.get(a)).post(rb).build();
            Response rsp = pic.newCall(request).execute();
            InputStream is = rsp.body().byteStream();
            bitmap = BitmapFactory.decodeStream(is);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}


