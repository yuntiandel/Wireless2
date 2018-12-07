package com.example.wireless;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DataBaseUtil.MyDatebaseHelper;
import bean.Login;
import bean.LoginGet;
import bean.RecordData;
import bean.SessionId;
import myapp.MyApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import util.Base64Utils;
import util.SharedPreferencesUtils;
import widget2.LoadingDialog;

import static android.content.ContentValues.TAG;
/**
 * 登录界面
 */
public class LoginActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //布局内的控件
    String exception = "null";
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private CheckBox checkBox_password;
//    private CheckBox checkBox_login;
    private ImageView iv_see_password;
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框
    EditText username  = null;
    EditText password  = null;
    String user;
    String pass;
    String data = "No";
    String sessionid = "";
    String ip;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //提前将需要监听的输入框准备好
        username = (EditText) findViewById(R.id.et_account);
        password = (EditText) findViewById(R.id.et_password);
        Log.d("12345654321", "开始执行");
        initViews();
        setupEvents();
        initData();


//        SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
//        ip = rs.getString("ipconfig");
//        Log.d("12345654321", "Login获得ip:"+ip);
        ip = getResources().getString(R.string.httpclient);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
        } else {

        }
    }
    private void initData() {
        //判断用户第一次登陆
        if (firstLogin()) {
            checkBox_password.setChecked(false);//取消记住密码的复选框
        }
        //判断是否记住密码
        if (remenberPassword()){
            checkBox_password.setChecked(true);//勾选记住密码
            setTextNameAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextName();//把用户账号放到输入账号的输入框中
        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextNameAndPassword() {
        if (!firstLogin()){
            Log.d("12345654321", "我执行了？？？？");
            et_name.setText("" + getLocalName());
            et_password.setText("" + getLocalPassword());
        }
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextName() {
        if (!firstLogin()){
            Log.d("12345654321", "我执行了？？");
            et_name.setText("" + getLocalName());
        }
    }

    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String name =""+ helper.getString("nameacc");
        if (name.equals("null")){
            return "";
        }else {
            return name;
        }
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

    /**
     * 判断是否自动登录
     */
    private boolean autoLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean autoLogin = helper.getBoolean("autoLogin", false);
        return autoLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean remenberPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean remenberPassword = helper.getBoolean("remenberPassword", false);
        return remenberPassword;
    }

    //将需要监听的控件设置好
    private void initViews() {
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
//        checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
    }
    //设置监听器
    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        checkBox_password.setOnCheckedChangeListener(this);
//        checkBox_login.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);
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
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUserName();    //无论如何保存一下用户名
                login(0); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;
        }
    }

    private void body(){
        //创建请求信息
        final OkHttpClient okHttpClient = new OkHttpClient();
        final SharedPreferencesUtils s = new SharedPreferencesUtils(this,"setting");
        Log.d("12345654321", "信息准备完毕！" );
        Log.d("12345654321", "地址："+R.string.httpclient );
        Log.d("12345654321", "地址："+getResources().getString(R.string.httpclient) );
//        String ip = s.getString("ipconfig");
        RequestBody requestBody = new FormBody.Builder()
                .add("userName", user)
                .add("passWord", pass)
                .add("Index", "0")
                .build();
        final Request request = new Request.Builder()
                .url("http://"+ip+"/Login/Login")
                .post(requestBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态
                try{
                    Log.d("12345654321", "我进来了！" );
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    Response response  = okHttpClient.newCall(request).execute();
                    Log.d("12345654321", "连接成功！" );
                    data = response.body().string();
                    Log.d("12345654321", "数据获取成功！");
                    Log.d("12345654321", data);
                    Gson gson = new Gson();
//                    String realjson = data.substring(1,data.length()-1);
//                    Log.d("12345654321", "数据："+ realjson );
                    java.lang.reflect.Type type = new TypeToken<LoginGet>(){}.getType();
                    LoginGet re  = gson.fromJson(data,type);
                    sessionid = re.getSessionID().toString();
                    name = re.getName().toString();
                    Log.d("12345654321", "数据："+ sessionid );
                    Log.d("12345654321", "数据："+ name );
                }catch (Exception e){
                    e.printStackTrace();
                    exception = e.toString();
                    Log.d("12345654321", "run: "+ e);
                }
                //判断账号和密码
                if (!exception.equals("null")){
                    showToast("登录失败，请重新检查！");
                    sleep();
//                       recreate();
                    setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                    hideLoading();//隐藏加载框
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }else if (data.equals("No")) {
                    showToast("用户或密码不正确");
                }else {
                    showToast("登录成功");
                    loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;
                    //保存用户的SessionId和网格员姓名
                    s.putValues(new SharedPreferencesUtils.ContentValue("sessionid",sessionid));
                    s.putValues(new SharedPreferencesUtils.ContentValue("name",name));
                    //登录成功，跳转主页
                    Log.d("12345654321", "拜拜了您呐！" );
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }
                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                hideLoading();//隐藏加载框
            }
        }).start();
    }

    private void zidong(){
        //创建请求信息
        final OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userName", user)
                .add("passWord", pass)
                .add("Index", "0")
                .build();
        final Request request = new Request.Builder()
                .url("http://"+getResources().getString(R.string.httpclient)+"/Login/Login")
                .post(requestBody)
                .build();
        final SharedPreferencesUtils s = new SharedPreferencesUtils(this,"setting");
        Log.d("12345654321", "信息准备完毕！" );
        Log.d("12345654321", "地址："+R.string.httpclient );
        Log.d("12345654321", "地址："+getResources().getString(R.string.httpclient) );
        new Thread(new Runnable() {
            @Override
            public void run() {
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态
                try{
                    Thread.sleep(3000);
                    Log.d("12345654321", "我进来了！" );
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    Response response  = okHttpClient.newCall(request).execute();
                    Log.d("12345654321", "连接成功！" );
                    data = response.body().string();
                    Log.d("12345654321", "数据获取成功！");
                    Log.d("12345654321", data);
                    Gson gson = new Gson();
//                    String realjson = data.substring(1,data.length()-1);
//                    Log.d("12345654321", "数据："+ realjson );
                    java.lang.reflect.Type type = new TypeToken<LoginGet>(){}.getType();
                    LoginGet re  = gson.fromJson(data,type);
                    sessionid = re.getSessionID().toString();
                    name = re.getName().toString();
                    Log.d("12345654321", "数据："+ sessionid );
                    Log.d("12345654321", "数据："+ name );
                }catch (Exception e){
                    e.printStackTrace();
                    exception = e.toString();
                    Log.d("12345654321", "run: "+ e);
                }
                //判断账号和密码
                if (!exception.equals("null")){
                    showToast("登录失败，请重新检查！");
                    sleep();
//                       recreate();
                    setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                    hideLoading();//隐藏加载框
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }else if (data.equals("No")) {
                    showToast("用户或密码不正确");
                }else {
                    showToast("登录成功");
                    loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;
                    //保存用户的SessionId和网格员姓名
                    s.putValues(new SharedPreferencesUtils.ContentValue("sessionid",sessionid));
                    s.putValues(new SharedPreferencesUtils.ContentValue("name",name));
                    //登录成功，跳转主页
                    Log.d("12345654321", "拜拜了您呐！" );
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();//关闭页面
                }
                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                hideLoading();//隐藏加载框
            }
        }).start();
    }
    /**
     * 模拟登录情况
     */
    private void login(int a) {
        Log.d("12345654321", "准备登录！" );
        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }
        if (getPassword().isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        showLoading();//显示加载框
        user = username.getText().toString();
        pass = password.getText().toString();
        body();
    }
    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("nameacc", getAccount()));
        }

    }


    private void sleep(){
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }


    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(checkBox_password);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password) {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

//        //如果设置自动登录
//        if (checkBox_login.isChecked()) {
//            //创建记住密码和自动登录是都选择,保存密码数据
//            helper.putValues(
//                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
//                    new SharedPreferencesUtils.ContentValue("autoLogin", true),
//                    new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));
//
//        } else
        if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));
        }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);
    }


    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        }
        mLoadingDialog.show();
    }


    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }


    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_password) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
//                checkBox_login.setChecked(false);
            }
        }
    }


    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
