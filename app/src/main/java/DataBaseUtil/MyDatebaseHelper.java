package DataBaseUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import bean.Login;
import bean.XunChaJiLu;


public class MyDatebaseHelper extends SQLiteOpenHelper {

    private Login login;
    public static final String LOGIN = "Login";
   // public static final String Create_ShiJianXinXi ="create table ShiJianXinXi("+"BianHao TEXT (12) NOT NULL ,"+"ShiJianDaiMa TEXT (4) NOT NULL,"+"ShiJianShuoMing TEXT (30),"+"DiDian TEXT (20),"+"JingDu TEXT (10),"+"WeiDu TEXT (10),"+"ShangBaoShiJian TEXT (14),"+"MBGLBiaoZhun TEXT (10),"+"WTZGQiXian TEXT (10),"+"TuPian TEXT (50),"+"BeiZhu TEXT (200),"+"Tag TEXT(12))";
    public static final String Create_Login ="create table Login("+"id_ TEXT (12) PRIMARY KEY UNIQUE NOT NULL,"+"username TEXT (20) UNIQUE NOT NULL,"+"password TEXT (20) NOT NULL,"+"flag     TEXT (2)  NOT NULL)";
    public static final String RENYUANXINXI =        "create table Renyuanxinxi(" + "Username text NOT NULL , " + "Userpwd text NOT NULL ," + "Xingming text," + "Zhize text," + "Xingbie text," + "Dianhua text," + "BianHao integer primary key not null," + "Tag integer," + "WangGeZhangXingming text," + "SuoShuWangGe text," + "SuoShuGongZuoZhan text )";
    public static final String Create_XunChaJiLu = "create table XunChaJiLu (BianHao integer primary key not null,XunChaRiQi text,XunChaQuyu text,ShiJianShuLiang integer,WGYXingming text,WGZXingming text,SSWangGe text,SSGongZuoZhan text)";
    public static final String CaoGaoXiang = "create table CaoGaoXiang (" + "ShiJianDaiMa integer" + "BianHao integer primary key not null," + "ShiJianShuoMing text," + "DiDian text," + "JingDu text," + "WeiDu text," + "ShangBaoShiJian text," + "ShiFouShangBao integer," + "SuoShuWangGe text," + "SuoShuGongZuoZhan text," + "QunZhongYiJian text," + "ChuZhiGuoCheng text," + "ChuZhiJieGuo text," + "TuPian text," + "BeiZhu text )";
    public static final String ShiJianXinXi = "create table ShiJianXinXi (" + "ShiJianDaiMa integer" + "BianHao integer primary key not null," + "ShiJianShuoMing text," + "DiDian text," + "JingDu text," + "WeiDu text," + "ShangBaoShiJian text," + "ShiFouShangBao integer," + "SuoShuWangGe text," + "SuoShuGongZuoZhan text," + "QunZhongYiJian text," + "ChuZhiGuoCheng text," + "ChuZhiJieGuo text," + "TuPian text," + "BeiZhu text )";

    private Context mContext;

    public MyDatebaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL(Create_ShiJianXinXi);
        db.execSQL(ShiJianXinXi);
        db.execSQL(Create_Login);
        db.execSQL(RENYUANXINXI);
        db.execSQL(Create_XunChaJiLu);
    }

    public void CreateTable (){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
    }
    public Login getOther (String username,SQLiteDatabase db){

        Cursor cursor = db.rawQuery("select * from Login where username = ?",new String[]{username});

        return login;
    }
}
