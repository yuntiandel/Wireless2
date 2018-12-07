package com.example.wireless;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import util.SharedPreferencesUtils;

/**
 * Created by 86188 on 2018/8/1.
 */

public class YiJianfankui extends AppCompatActivity {

    public EditText editText;
    public Button button;
    SharedPreferencesUtils rs ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yijian_fankui);
        rs = new SharedPreferencesUtils(this,"setting");
        editText = (EditText) findViewById(R.id.yijian_word);
        button = (Button) findViewById(R.id.tijiao);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String a = getResources().getString(R.string.httpclient);
                    editText.setText("123+"+ a);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("12345654321", "数据错误："+ e );
                }

            }
        });

    }
}
