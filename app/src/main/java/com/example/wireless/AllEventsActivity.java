package com.example.wireless;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.TimeZone;

import DataBaseUtil.MyDatebaseHelper;
import bean.AllEventShow;
import bean.ShiJianXinXi;
import bean.ShiJianXinXiGet;
import myadapter.AllShiJianAdapter;
import myadapter.MyEventAdapter;
import myapp.MyApplication;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.SharedPreferencesUtils;

public class AllEventsActivity extends AppCompatActivity {

    private final int GETHTTP = 0;
    private final int LAST = 1;
    private final int NEXT = 2;
    private final int ITEMCLICK = 3;
    String sessionid = "";
    String ip;
    List<ShiJianXinXiGet> xinXiGets;
    int yeshu = 0;
    int count = 0;
    ListView listView;
    TextView textView;
    List<ShiJianXinXiGet> listaaa;
    List<ShiJianXinXiGet> parts;
    int anInt;
    String[] s;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETHTTP:
                    getMessage();
                    break;
                case LAST:
                    shangyiye();
                    break;
                case NEXT:
                    xiayiye();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevents);
        Button left = (Button)findViewById(R.id.shangyiye);
        Button right = (Button)findViewById(R.id.xiayiye);
        textView = (TextView)findViewById(R.id.yeshu);
        anInt = Integer.parseInt(textView.getText().toString());
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
//        获取到sessionid
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
        Log.d("12345654321", "sessionid:" + sessionid );
        //清空list，防止重复注入
        if (xinXiGets != null){
            xinXiGets.clear();
        }
        Message message = new Message();
        message.what = GETHTTP;
        handler.sendMessage(message);

//        Log.d("12345654321", "List数据处理完成！");
        //将数据显示到listView中
//        AllShiJianAdapter allShiJianAdapter = new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,xinXiGets);
        listView = (ListView)findViewById(R.id.list_event);

        //上一页
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message1 = new Message();
                        message1.what = LAST;
                        handler.sendMessage(message1);
//                        List<ShiJianXinXiGet> list2 =  fanye(xinXiGets,0);
//                        if (list2==null){
//                            Log.d("12345654321", "kong1！" );
//                        }
//                        AllShiJianAdapter a2 = new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,list2);
//                        if (a2==null){
//                            Log.d("12345654321", "kong！" );
//                        }
//                        Log.d("12345654321", "竟然到这里了！" );
//                        listView.setAdapter(a2);
                    }
                }).start();

            }
        });
        //下一页
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message2 = new Message();
                        message2.what = NEXT;
                        handler.sendMessage(message2);
                    }
                }).start();

            }
        });
    }

    //从服务端获取信息
    private void getMessage(){
                try{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    OkHttpClient Client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("pagestart",""+ 1)
                            .add("pageend",""+10)
                            .build();
                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                            .url("http://"+ip+"/MainWangGe/MWGleftlist2_fenye")
                            .post(requestBody)
                            .build();
                    Log.d("12345654321", "准备好发请求！" );
                    Response response = Client.newCall(request).execute();
                    String yuansheng = response.body().string();
                    Log.d("12345654321", "获得数据:" + yuansheng );
                    String realjson = yuansheng.substring(1,yuansheng.length()-1);
                    String realjson2 = realjson.substring(1,realjson.length()-1);
                    String[] s = realjson2.split("\\},\\{");
                    Log.d("12345654321", "数组长度："+ s.length );
                    Log.d("12345654321", "切分成功！" );
                    Log.d("12345654321", "输出一下->" );
                    xinXiGets = new ArrayList<ShiJianXinXiGet>();
                    Log.d("12345654321", "创建成功！" );
                    int mm;
                    if (s.length>10){
                        mm = 10;
                    }else {
                        mm = s.length;
                    }
                    for (int i = 0;i<mm;i++){
                        Log.d("12345654321", "成功进入！" );
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                        ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                        Log.d("12345654321", "数据："+ x.toString() );
                        xinXiGets.add(x);
                        count ++;
                        Log.d("12345654321", "添加成功");
                    }
                    Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
                    Log.d("12345654321", "开始装入！" );
                    AllShiJianAdapter a = new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,xinXiGets);
                    listView.setAdapter(a);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("12345654321", "开始装入!");
                            ShiJianXinXiGet sss = xinXiGets.get(position);
                            Log.d("12345654321", "开始装入:" + position );

                            Intent intent = new Intent(AllEventsActivity.this,DoubleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shijianxinxi",sss);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("12345654321", "出错了！->" + e);
                }
    }


    private void getMessage_fenye(){
        try{
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            OkHttpClient Client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                    .url("http://"+getResources().getString(R.string.httpclient)+"/MainWangGe/MWGleftlist2_phone")
                    .post(requestBody)
                    .build();
            Log.d("12345654321", "准备好发请求！" );
            Response response = Client.newCall(request).execute();
            String yuansheng = response.body().string();
            Log.d("12345654321", "获得数据:" + yuansheng );
            String realjson = yuansheng.substring(1,yuansheng.length()-1);
            String realjson2 = realjson.substring(1,realjson.length()-1);
            String[] s = realjson2.split("\\},\\{");
            Log.d("12345654321", "数组长度："+ s.length );
            Log.d("12345654321", "切分成功！" );
            Log.d("12345654321", "输出一下->" );
            Log.d("12345654321", "数据："+s[0] );
            Log.d("12345654321", "数据："+s[1] );
            Log.d("12345654321", "数据："+s[2] );
            Log.d("12345654321", "数据："+s[3] );
            xinXiGets = new ArrayList<ShiJianXinXiGet>();
            Log.d("12345654321", "创建成功！" );
            for (int i = 0;i<s.length;i++){
                Log.d("12345654321", "成功进入！" );
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                Log.d("12345654321", "数据："+ x.toString() );
                xinXiGets.add(x);
                count ++;
                Log.d("12345654321", "添加成功");
            }
            Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
            Log.d("12345654321", "开始装入！" );
            AllShiJianAdapter a = new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,xinXiGets);
            listView.setAdapter(a);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int x = position + 7*yeshu;
                    Log.d("12345654321", "开始装入:" + x);
                    ShiJianXinXiGet sss = xinXiGets.get(x);
                    Log.d("12345654321", "开始装入:" + position +"--"+ 7*yeshu);

                    Intent intent = new Intent(AllEventsActivity.this,DoubleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shijianxinxi",sss);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("12345654321", "出错了！->" + e);
        }
    }

    //上一页
    private void shangyiye(){
        anInt = Integer.parseInt(textView.getText().toString());
        if(anInt == 1){
            showToast("当前为第一页！");
        }else {
            try{
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                OkHttpClient Client = new OkHttpClient();
                int a = (anInt-1)*10;
                Log.d("12345654321", "请求a:" + a );
                RequestBody requestBody = new FormBody.Builder()
                        .add("pagestart",""+ (a-9))
                        .add("pageend",""+a)
                        .build();
                Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                        .url("http://"+ip+"/MainWangGe/MWGleftlist2_fenye")
                        .post(requestBody)
                        .build();
                Log.d("12345654321", "准备好发请求！" );
                Response response = Client.newCall(request).execute();
                String yuansheng = response.body().string();
                Log.d("12345654321", "获得数据:" + yuansheng );
                String realjson = yuansheng.substring(1,yuansheng.length()-1);
                String realjson2 = realjson.substring(1,realjson.length()-1);
                String[] s = realjson2.split("\\},\\{");
                Log.d("12345654321", "数组长度："+ s.length );
                Log.d("12345654321", "切分成功！" );
                Log.d("12345654321", "输出一下->" );
                xinXiGets = new ArrayList<ShiJianXinXiGet>();
                Log.d("12345654321", "创建成功！" );
                for (int i = 0;i<s.length;i++){
                    Log.d("12345654321", "成功进入！" );
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                    ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                    Log.d("12345654321", "数据："+ x.toString() );
                    xinXiGets.add(x);
                    count ++;
                    Log.d("12345654321", "添加成功");
                }
                Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
                Log.d("12345654321", "开始装入！" );
                AllShiJianAdapter all= new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,xinXiGets);
                listView.setAdapter(all);
                textView.setText(""+(anInt-1));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("12345654321", "开始装入！");
                        ShiJianXinXiGet sss = xinXiGets.get(position);
                        Log.d("12345654321", "开始装入:" + position );
                        Intent intent = new Intent(AllEventsActivity.this,DoubleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("shijianxinxi",sss);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //下一页
    private void xiayiye(){
        anInt  = Integer.parseInt(textView.getText().toString());
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            OkHttpClient Client = new OkHttpClient();
            int a = anInt*10 +1;
            Log.d("12345654321", "请求a:" + a);
            RequestBody requestBody = new FormBody.Builder()
                    .add("pagestart", "" + a)
                    .add("pageend", "" + a+9)
                    .build();
            Request request = new Request.Builder().addHeader("Cookie", "ASP.NET_SessionId=" + sessionid)
                    .url("http://"+ip+"/MainWangGe/MWGleftlist2_fenye")
                    .post(requestBody)
                    .build();
            Log.d("12345654321", "准备好发请求！");
            Response response = Client.newCall(request).execute();
            String yuansheng = response.body().string();

            Log.d("12345654321", "获得数据:" + yuansheng);
            if (yuansheng.equals("[]")){
                showToast("当前为最后一页！");
            }else {
                String realjson = yuansheng.substring(1, yuansheng.length() - 1);
                String realjson2 = realjson.substring(1, realjson.length() - 1);
                String[] sx= realjson2.split("\\},\\{");
                Log.d("12345654321", "数组长度：" + s.length);
                textView.setText(""+(anInt+1));
                Log.d("12345654321", "切分成功！");
                Log.d("12345654321", "输出一下->");
                xinXiGets = new ArrayList<ShiJianXinXiGet>();
                Log.d("12345654321", "创建成功！" );
                for (int i = 0;i<sx.length+1;i++){
                    Log.d("12345654321", "成功进入！" );
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                    ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                    Log.d("12345654321", "数据："+ x.toString() );
                    xinXiGets.add(x);
                    count ++;
                    Log.d("12345654321", "添加成功");
                }
            }
            Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
            Log.d("12345654321", "开始装入！" );
            AllShiJianAdapter all = new AllShiJianAdapter(AllEventsActivity.this,R.layout.allitem,xinXiGets);
            listView.setAdapter(all);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("12345654321", "开始装入！");
                    ShiJianXinXiGet sss = xinXiGets.get(position);
                    Log.d("12345654321", "开始装入:" + position );
                    Intent intent = new Intent(AllEventsActivity.this,DoubleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shijianxinxi",sss);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "出错：" + e);
        }
    }

    //时间转换：从数据库中读取到的是（Date(00000)）类型的数据，在向view投影前转换成正确的数据(废弃)
    //(换成在adapter中change)
    private String changetime(String time){
        SimpleDateFormat fff = new SimpleDateFormat("yyyyMMdd");
        fff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String x = fff.format(time);
        return x;
    }
    //翻页
    private List<ShiJianXinXiGet> fanye(List<ShiJianXinXiGet> all,int a){
        final List<ShiJianXinXiGet> part = all;
//        List<ShiJianXinXiGet> parts;
        final int x = a;
       new Thread(new Runnable() {
           @Override
           public void run() {

               //判断输入的值，0为上一页
               if (x == 0){
                   //判断当前是不是第一页，第一页就从0开始取数
                   if (yeshu == 0){
                       showToast("当前为第一页");
                       for (int i = 0;i<7;i++){
                           //有可能一页不满
                           try{
                               //从全部的事件里取数到显示的数据中
                               parts.add(part.get(i));
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }
                   }else {
                       for (int i = 7 * (yeshu-1);i<7;i++){
                           try{
                               parts.add(part.get(i));
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }
                       yeshu--;
                   }
               }
               //不是0跳下一页
               else {
                   if (yeshu+1 >=count/7 +1){
                       showToast("当前为最后一页");
                   }else {
                       if (yeshu+1 <=count/7 +1){
                           for (int w = 7 * (yeshu + 1); w<7;w++){
                               try{
                                   parts.add(part.get(w));
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                           }
                           yeshu ++;
                       }
                   }

               }

           }
       }).start();
        return part;
    }


    //请求事件并返回
    private List<ShiJianXinXiGet> getMessages(){
        List<ShiJianXinXiGet> lists = null;
        try{
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            OkHttpClient Client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().build();
            Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
                    .url("http://"+getResources().getString(R.string.httpclient)+"/MainWangGe/MWGleftlist2")
                    .post(requestBody)
                    .build();
            Log.d("12345654321", "准备好发请求！" );
            Response response = Client.newCall(request).execute();
            String yuansheng = response.body().string();
            Log.d("12345654321", "获得数据:" + yuansheng );
            String realjson = yuansheng.substring(1,yuansheng.length()-1);
            String realjson2 = realjson.substring(1,realjson.length()-1);
            String[] s = realjson2.split("\\},\\{");
            Log.d("12345654321", "切分成功！" );
            lists = new ArrayList<ShiJianXinXiGet>();
            Log.d("12345654321", "创建成功！" );
            for (int i = 0;i<s.length-1;i++){
                Log.d("12345654321", "成功进入！" );
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                Log.d("12345654321", "数据："+ x.toString() );
                lists.add(x);
                count ++;
                Log.d("12345654321", "添加成功");
            }
            Log.d("12345654321", "信息获取完毕！" );
        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "出错了！->"+ e );
        }
        return lists;
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AllEventsActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
