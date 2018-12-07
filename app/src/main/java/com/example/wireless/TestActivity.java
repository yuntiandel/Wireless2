package com.example.wireless;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import util.SharedPreferencesUtils;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        String data;
        final SharedPreferencesUtils rs = new SharedPreferencesUtils(this,"setting");
        final Context context = this;
        final TextView textView = (TextView)findViewById(R.id.iptestedit);
        Button btn = (Button)findViewById(R.id.iptestbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = textView.getText().toString();
                rs.putValues(new SharedPreferencesUtils.ContentValue("ipcocnfig",edit));
                Log.d("12345654321", "Test获得ip:"+edit);
                String ips = rs.getString("ipconfig");
                Log.d("12345654321", "Test获得ips:"+ips);
                Intent intent = new Intent(TestActivity.this,StartActivity.class);
                intent.putExtra("ipconfigs",""+edit);
                startActivity(intent);
            }
        });

    }
}
