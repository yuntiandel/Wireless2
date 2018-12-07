package com.example.wireless;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
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
import bean.MyEventShow;
import bean.RecordData;
import bean.SerializableMap;
import bean.ShiJianXinXi;
import bean.ShiJianXinXiGet;
import myadapter.AllShiJianAdapter;
import myadapter.MyEventAdapter;
import myadapter.MyShiJianAdapter;
import myapp.MyApplication;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import util.SharedPreferencesUtils;


public class MyEventsActivity extends AppCompatActivity{

    /*
    * 使用MVP模式读取储存在本地数据库的的数据
    * 然后以列表形式显示在界面上，单条item信息包含事件类型（即事件编号），
    * 事件地点，和事件上传时间，点击单条item可以观看时间的详细信息
    * 实现方式为获取登录时的网格员id，根据id检索事件并显示
    * */

    //如何向下一个活动传递本活动查出的数据
    /*
    * 解决办法：把查询的数据按属性挨个放到map中，然后将这些map封装成一个list集合
    * 然后，再向下一个活动传递时，根据item的编号获取list中的map，使用Bundle封装这个map，
    * 然后使用intent传递，这样避免了在下一个活动中继续开启数据库查询
    * */
    private final int GETHTTPMY = 0;
    private final int LASTMY = 1;
    private final int NEXTMY = 2;
    private final int ITEMCLICK = 3;
    private ArrayList<Integer> numberList = new ArrayList<>();
    private MyEventAdapter myEventAdapter;
    private List<Map<String,Object>> eventList = new ArrayList<Map<String, Object>>();
    private int columncount;
    private MyDatebaseHelper datebaseHelper;
    private SQLiteDatabase sd;
    public String getID;
    public String getFlag;
    ResultSet rs = null;
    JSONArray ja = new JSONArray();
    String sessionid = "";
    String ip;
    String name = "";
    String[] s;
    List<ShiJianXinXiGet> xinXiGets;
    ListView listView;
    TextView textView;
    List<ShiJianXinXiGet> myevent;
    int yeshu = 0;
    int count = 0;
    int anInt;

    private Handler handlermy = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETHTTPMY:
                    getMessage();
                    break;
                case LASTMY:
                    shangyiye();
                    break;
                case NEXTMY:
                    xiayiye();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myevents);
        Button left = (Button)findViewById(R.id.shangyiyemy);
        Button rightmy = (Button)findViewById(R.id.xiayiyemy);
        textView = (TextView)findViewById(R.id.yeshumy);
        //获取sessionid和name,便于判断是否为自己的事件
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
        name = rs.getString("name");
        //清空list，防止重复注入
        if (myevent != null){
            myevent.clear();
        }
        if (xinXiGets != null){
            xinXiGets.clear();
        }
        Message message = new Message();
        message.what = GETHTTPMY;
        handlermy.sendMessage(message);
//        getMessage();
        //发起网络请求
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//                    OkHttpClient Client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder().build();
//                    Request request = new Request.Builder().addHeader("Cookie","ASP.NET_SessionId="+sessionid)
//                            .url("http://192.168.1.236:8010/MainWangGe/MWGleftlist2")
//                            .post(requestBody)
//                            .build();
//                    Log.d("12345654321", "我准备好了！" );
//                    Response response = Client.newCall(request).execute();
//                    String yuansheng = response.body().string();
//                    Log.d("12345654321", "获得数据:" + yuansheng );
//                    //因为获得的数据是[{"","",""}]的样式
//                    //所以要用两次处理获得""},{""},{""的数据，方便以 },{ 的方式切分
//                    String realjson = yuansheng.substring(1,yuansheng.length()-1);
//                    String realjson2 = realjson.substring(1,realjson.length()-1);
//                    s = realjson2.split("\\},\\{");
//                    Log.d("12345654321", "切分成功！" );
//                    Log.d("12345654321", "输出一下->" );
//                    Log.d("12345654321", "数据："+s[0] );
//                    Log.d("12345654321", "数据："+s[1] );
//                    Log.d("12345654321", "数据："+s[2] );
//                    Log.d("12345654321", "数据："+s[3] );
//                    xinXiGets = new ArrayList<ShiJianXinXiGet>();
//                    Log.d("12345654321", "创建成功！" );
//                    for (int i = 0;i<s.length-1;i++){
//                        Log.d("12345654321", "成功进入！" );
//                        Gson gson = new Gson();
//                        java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
//                        //首尾加上括号之后就成为一个对象的格式
//                        ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
//                        Log.d("12345654321", "数据："+ x.toString() );
//                        xinXiGets.add(x);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.d("12345654321", "出错了！->"+ e );
//                }
//            }
//        }).start();
        //判断name是不是之前保存的名字，判断是不是当前网格员的事件
//        for (int i = 0;i<s.length-1;i++){
//            if (name.equals(xinXiGets.get(i).getName())){
//                myevent.add(xinXiGets.get(i));
//            }
//        }
        //数据注入
//        MyShiJianAdapter myEventAdapter = new MyShiJianAdapter(MyEventsActivity.this,R.layout.item,myevent);
        listView = (ListView)findViewById(R.id.mylist_event);
//        listView.setAdapter(myEventAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                AllEventShow allEventShow = allEventShows.get(position);
//                ShiJianXinXiGet sss = myevent.get(position);
//                String[] send = {
//                        sss.getShiJianBianHao(),
//                        sss.getXunChaJiLu(),
//                        sss.getShiJianXiaoLei(),
//                        sss.getShiJianShuoMing(),
//                        sss.getShiJianDiDian(),
//                        sss.getJingDu(),
//                        sss.getWeiDu(),
//                        sss.getShiFouShangBao(),
//                        sss.getShangBaoShiJian(),
//                        sss.getShangBaoBuMen(),
//                        sss.getChuZhiGuoCheng(),
//                        sss.getChuLiJieGuo(),
//                        sss.getQunZhongYiJian(),
//                        sss.getGenJinCuoShi(),
//                        sss.getTuPian(),
//                        sss.getBeiZhu(),
//                        sss.getBMFZRSHYiJian(),
//                        sss.getName()
//                };
//                Intent intent = new Intent();
//                intent.setClass(MyEventsActivity.this,DoubleActivity.class);
//                intent.putExtra("ShiJianXinXi",send);
//                startActivity(intent);
//            }
//        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = LASTMY;
                handlermy.sendMessage(message);
            }
        });
        rightmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = NEXTMY;
                handlermy.sendMessage(message);
            }
        });
    }


    //从服务端获取信息
    private void getMessage(){
        anInt  = Integer.parseInt(textView.getText().toString());
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
                    Log.d("12345654321", "我准备好了！" );
                    Response response = Client.newCall(request).execute();
                    String yuansheng = response.body().string();
                    Log.d("12345654321", "获得数据:" + yuansheng );
                    String realjson = yuansheng.substring(1,yuansheng.length()-1);
                    String realjson2 = realjson.substring(1,realjson.length()-1);
                    s = realjson2.split("\\},\\{");
                    Log.d("12345654321", "切分成功！" );
                    Log.d("12345654321", "输出一下->" );
                    xinXiGets = new ArrayList<ShiJianXinXiGet>();
                    myevent = new ArrayList<ShiJianXinXiGet>();
                    Log.d("12345654321", "创建成功！" );
                    int s0;
                    if (s.length>10){
                        s0 = 10;
                    }else {
                        s0 = s.length;
                    }
                    for (int i = 0;i<s0;i++){
                        Log.d("12345654321", "成功进入！" );
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                        ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                        Log.d("12345654321", "数据："+ x.toString() );
                        xinXiGets.add(x);
                        Log.d("12345654321", "数据name："+ x.getName());
                        //判断name是不是之前保存的名字，判断是不是当前网格员的事件
                        if (x.getName().equals(name)){
                            myevent.add(x);
                        }
                    }
                    Log.d("12345654321", "size："+ xinXiGets.size());
                    Log.d("12345654321", "size："+ myevent.size());
                    MyShiJianAdapter myEventAdapter = new MyShiJianAdapter(MyEventsActivity.this,R.layout.item,myevent);
                    listView.setAdapter(myEventAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ShiJianXinXiGet sss = myevent.get(position);
                            Log.d("12345654321", "开始装入:" + position );
                            Intent intent = new Intent(MyEventsActivity.this,DoubleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shijianxinxi",sss);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("12345654321", "出错了0！->"+ e );
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
                myevent = new ArrayList<ShiJianXinXiGet>();
                Log.d("12345654321", "创建成功！" );
                for (int i = 0;i<s.length;i++){
                    Log.d("12345654321", "成功进入！" );
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                    ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                    Log.d("12345654321", "数据："+ x.toString() );
                    xinXiGets.add(x);
                    Log.d("12345654321", "数据name："+ x.getName());
                    //判断name是不是之前保存的名字，判断是不是当前网格员的事件
                    if (x.getName().equals(name)){
                        myevent.add(x);
                    }
                    count ++;
                    Log.d("12345654321", "添加成功");
                }
                Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
                Log.d("12345654321", "信息获取完毕："+myevent.size() );
                Log.d("12345654321", "开始装入！" );
                AllShiJianAdapter all= new AllShiJianAdapter(MyEventsActivity.this,R.layout.allitem,myevent);
                listView.setAdapter(all);
                textView.setText(""+(anInt-1));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("12345654321", "开始装入！");
                        ShiJianXinXiGet sss = myevent.get(position);
                        Log.d("12345654321", "开始装入:" + position );
                        Intent intent = new Intent(MyEventsActivity.this,DoubleActivity.class);
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
                s = realjson2.split("\\},\\{");
                Log.d("12345654321", "数组长度：" + s.length);
                textView.setText(""+(anInt+1));
                Log.d("12345654321", "切分成功！");
                Log.d("12345654321", "输出一下->");
            }
            xinXiGets = new ArrayList<ShiJianXinXiGet>();
            myevent = new ArrayList<ShiJianXinXiGet>();
            Log.d("12345654321", "创建成功！" );
            for (int i = 0;i<s.length;i++){
                Log.d("12345654321", "成功进入！" );
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<ShiJianXinXiGet>(){}.getType();
                ShiJianXinXiGet x  = gson.fromJson("{"+s[i]+"}",type);
                Log.d("12345654321", "数据："+ x.toString() );
                xinXiGets.add(x);
                //判断name是不是之前保存的名字，判断是不是当前网格员的事件
                if (x.getName().equals(name)){
                    myevent.add(x);
                }
                count ++;
                Log.d("12345654321", "添加成功");
            }
            Log.d("12345654321", "信息获取完毕："+xinXiGets.size() );
            Log.d("12345654321", "信息获取完毕："+myevent.size() );
            Log.d("12345654321", "开始装入！" );
            MyShiJianAdapter all = new MyShiJianAdapter(MyEventsActivity.this,R.layout.allitem,myevent);
            listView.setAdapter(all);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("12345654321", "开始装入！");
                    ShiJianXinXiGet sss = myevent.get(position);
                    Log.d("12345654321", "开始装入:" + position );
                    Intent intent = new Intent(MyEventsActivity.this,DoubleActivity.class);
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


    //时间转换：从数据库中读取到的是（Date(00000)）类型的数据，在向view投影前转换成正确的数据
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
    //将未经处理的ShiJianXinXiGet对象，处理成AllEventShow便于显示
    private void DealWithData(List<MyEventShow> a,List<ShiJianXinXiGet> s){
        for (int i = 0;i<s.size()-1;i++){
            String time = changetime(s.get(i).getShangBaoShiJian());
            MyEventShow as = new MyEventShow(time,s.get(i).getShiJianDiDian().toString(),s.get(i).getShiJianShuoMing().toString());
            a.add(as);
        }
    }

    @Override
    protected void onDestroy() {
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }

    //初始化适配器
    private void initAdapter(){
//        myEventAdapter = new MyEventAdapter(this,sortList,locationList,timeList);
    }

    //初始化布局

    private  void initView(){
        listView = (ListView) findViewById(R.id.list_event);
        listView.setAdapter(myEventAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyEventsActivity.this,SingleEventActivity.class);
                final SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap(eventList.get(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("event",serializableMap);
                intent.putExtra("every",bundle);
                startActivity(intent);
            }
        });
    }

    //判断显示类型
    private String sortJuge(String s){
        if (s.equals("1")){
            s="公共安全";
        }
        else if (s.equals("2")){
            s="综治维稳";
        }
        else {
            s="生态环保";
        }
        return s;
    }

    //翻页
    private List<ShiJianXinXiGet> fanye(List<ShiJianXinXiGet> all,int a){
        List<ShiJianXinXiGet> part = null;
        //判断输入的值，0为上一页
        if (a == 0){
            //判断当前是不是第一页，第一页就从0开始取数
            if (yeshu == 0){
                for (int i = 0;i<7;i++){
                    //有可能一页不满
                    try{
                        //从全部的事件里取数到显示的数据中
                        part.add(all.get(i));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                for (int i = 7 * (yeshu-1);i<7;i++){
                    try{
                        part.add(all.get(i));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                yeshu--;
            }
        }
        //不是0跳下一页
        else {
            if (yeshu+1 <=count/7 +1){
                for (int w = 7 * (yeshu + 1); w<7;w++){
                    try{
                        part.add(all.get(w));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                yeshu ++;
            }
        }
        return part;
    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyEventsActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

