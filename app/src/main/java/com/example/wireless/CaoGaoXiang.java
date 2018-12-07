package com.example.wireless;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import DataBaseUtil.MyDatebaseHelper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.SharedPreferencesUtils;

public class CaoGaoXiang extends AppCompatActivity {
    String shangbao01;
    Spinner leixing;
    Spinner xiaolei;
    String [] shijianleixing = new String[]{"综治维稳","公共安全","生态环保"};
    String [] xiaolei1 = new String[]{"矛盾纠纷","信访","重点人员","特殊人群","治安邢事案件","突发问题"};
    String [] xiaolei2 = new String[]{"非法违法生产经营活动","道路交通","火灾","建筑施工","电梯","校车"};
    String [] xiaolei3 = new String[]{"空气","水","环境生态","噪声"};
    //正式
    private String bianhao;//编号
    private String xunchajilu;//巡查记录
    private String shifoushangbao;//是否上报
    private RadioGroup shifoushangbaoE;
    private RadioButton yes;
    private RadioButton no;
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
    String sessionid = "";
    String ip;
    String x1;
    String x2;
    String x3;
    String x4;
    String x5;
    String x6;
    String x7;
    String x8;
    String x9;
    String x10;
    String x11;
    String x12;
    String x13;
    String x14;
    String x15;
    List<Bitmap> imgList;
    private MyDatebaseHelper dbhelper = new MyDatebaseHelper(this,"WangGe.db",null,3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cao_gao_xiang);
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        sessionid = rs.getString("sessionid");
//        ip = rs.getString("ipconfig");
        ip = getResources().getString(R.string.httpclient);
        rs.putValues(new SharedPreferencesUtils.ContentValue("shijiancount"+getTime(new Date()),0));

//        shangbaobumenE = (EditText)findViewById(R.id.shijianleixing);
        shangbaodidianE = (EditText)findViewById(R.id.DiDianEcc);
        shijianshuomingE = (EditText)findViewById(R.id.ShiJianShuoMingEcc);
        chuzhiguochengE = (EditText)findViewById(R.id.ChuZhiGuoChengEcc);
        chuzhijieguoE = (EditText)findViewById(R.id.ChuzhiJieguoEcc);
        qunzhongyijianE = (EditText)findViewById(R.id.QunzhongyijianEcc);
        beizhuE = (EditText)findViewById(R.id.BeiZhuEcc);
        submits = (Button)findViewById(R.id.submitcc);
        shifoushangbaoE = (RadioGroup)findViewById(R.id.shijianshangbaocc);
        yes = (RadioButton)findViewById(R.id.yesshangbaocc);
        no = (RadioButton)findViewById(R.id.noshangbaocc);
        spinner = (Spinner)findViewById(R.id.spinnercc);
        spinner2 = (Spinner)findViewById(R.id.spinner0cc);
        try{
            x1 = rs.getString("0");
            x2 = rs.getString("1");
            x3 = rs.getString("2");
            x4 = rs.getString("3");
            x5 = rs.getString("4");
            x6 = rs.getString("5");
            x7 = rs.getString("6");
            x8 = rs.getString("7");
            x9 = rs.getString("8");
            x10 = rs.getString("9");
            x11 = rs.getString("10");
            x12 = rs.getString("11");
            x13 = rs.getString("12");
            x14 = rs.getString("13");
            x15 = rs.getString("14");
//            shangbaobumenE.setText(x1);
            shangbaodidianE.setText(x5);
            shijianshuomingE.setText(x10);
            chuzhiguochengE.setText(x11);
            chuzhijieguoE.setText(x12);
            beizhuE.setText(x15);
            qunzhongyijianE.setText(x13);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("12345654321", "数据出错："+ e );
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,shijianleixing);
        final List<ArrayAdapter<String>> adapterList = new ArrayList<ArrayAdapter<String>>();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,xiaolei3);
        adapterList.add(adapter1);
        adapterList.add(adapter2);
        adapterList.add(adapter3);

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
        final RadioGroup radioGroup  = (RadioGroup) findViewById(R.id.shijianshangbaocc);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton shangbaobutton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                shangbao01 = shangbaobutton.getText().toString();
            }
        });
        submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    bianhao = x1;
                    Log.d("12345654321", "数据编号："+bianhao );
                    xunchajilu = x2;
                    Log.d("12345654321", "数据巡查记录："+xunchajilu );
                    shifoushangbao =""+ shangbao01;
                    Log.d("12345654321", "数据是否上报："+shifoushangbao );
                    shangbaoshijian = getTime(new Date());
                    Log.d("12345654321", "数据上报时间："+shangbaoshijian );
                    shijiandidian = shangbaodidianE.getText().toString();
                    Log.d("12345654321", "数据上报地点："+shijiandidian );
                    JingDu = x6;
                    Log.d("12345654321", "数据经度："+JingDu );
                    WeiDu = x7;
                    Log.d("12345654321", "数据纬度："+WeiDu );

                    Log.d("12345654321", "数据上报部门："+spinner.getSelectedItem().toString() );

                    Log.d("12345654321", "数据小类："+spinner2.getSelectedItem().toString() );
                    shijianshuoming = shijianshuomingE.getText().toString();
                    Log.d("12345654321", "数据事件说明："+shijianshuoming );
                    chuzhiguocheng = chuzhiguochengE.getText().toString();
                    Log.d("12345654321", "数据处置过程："+chuzhiguocheng );
                    chuzhijieguo = chuzhijieguoE.getText().toString();
                    Log.d("12345654321", "数据处置结果："+chuzhijieguo );
                    qunzhongyijian = qunzhongyijianE.getText().toString();
                    Log.d("12345654321", "数据群众意见："+qunzhongyijian );
                    beizhu = beizhuE.getText().toString();
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
                                        .add("JingDu",JingDu)
                                        .add("WeiDu",WeiDu)
                                        .add("ShangBaoBuMen",spinner.getSelectedItem().toString())
                                        .add("ShiJianXiaoLei",spinner2.getSelectedItem().toString())
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
                                Log.d("12345654321", "我获取到了！" + con.substring(0,4) );
                                if (con.equals('"'+"Yes"+'"')){
                                    showToast("提交成功！");
                                    rs.putValues(new SharedPreferencesUtils.ContentValue("shijiancount"+getTime(new Date()),rs.getInt("shijiancount"+getTime(new Date()))+1),
                                            new SharedPreferencesUtils.ContentValue("0",""),
                                            new SharedPreferencesUtils.ContentValue("1",""),
                                            new SharedPreferencesUtils.ContentValue("2",""),
                                            new SharedPreferencesUtils.ContentValue("3",""),
                                            new SharedPreferencesUtils.ContentValue("4",""),
                                            new SharedPreferencesUtils.ContentValue("5",""),
                                            new SharedPreferencesUtils.ContentValue("6",""),
                                            new SharedPreferencesUtils.ContentValue("7",""),
                                            new SharedPreferencesUtils.ContentValue("8",""),
                                            new SharedPreferencesUtils.ContentValue("9",""),
                                            new SharedPreferencesUtils.ContentValue("10",""),
                                            new SharedPreferencesUtils.ContentValue("11",""),
                                            new SharedPreferencesUtils.ContentValue("12",""),
                                            new SharedPreferencesUtils.ContentValue("13",""),
                                            new SharedPreferencesUtils.ContentValue("14","")
                                    );
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(CaoGaoXiang.this,Main2Activity.class);
                                    startActivity(intent);
                                } else if (con.trim().substring(0,5).equals("<!DOC")){
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
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CaoGaoXiang.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
