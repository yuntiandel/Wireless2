package com.example.wireless;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import java.io.File;
import java.math.BigDecimal;
import util.SharedPreferencesUtils;

public class MainSettings extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);

        //清除缓存
        btn3 = (Button) findViewById(R.id.qingchuhuancun);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getCacheDir();
                deleteFileByDirectory(file);
                Toast.makeText(MainSettings.this,"缓存清理完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void deleteFileByDirectory(File directory){
        if(directory != null && directory.exists() && directory.isDirectory()){
            for (File item :directory.listFiles()){
                item.delete();
            }
        }
    }
}




