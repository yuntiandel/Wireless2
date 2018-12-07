package myapp;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {
    private String id_=null;
    private String flag = null;
    private int dbVersion = 1;
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public void addItem(String mid_, String mflag){
        id_  = mid_;
        flag = mflag;
    }
    public void addVersion(int mDbVersion){
        dbVersion = mDbVersion;
    }
    public HashMap<String, String> getItem(){
        String mid_ = id_;
        String mflag = flag;
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("id_", mid_);
        map.put("flag", mflag);
        return map;

    }

    public void removeItem(int index){

    }
    public void cleanItem(){
        id_ =  null;
        flag = null;
    }
}
