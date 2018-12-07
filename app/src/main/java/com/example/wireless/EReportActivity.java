package com.example.wireless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ereport);

        Button Version = (Button) findViewById(R.id.version);
        Version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EReportActivity.this,VersionIntroduct.class);
                startActivity(intent);
            }
        });

        Button callus = (Button) findViewById(R.id.callus);
        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EReportActivity.this,CallUs.class);
                startActivity(intent);
            }
        });
//
        Button yijian = (Button) findViewById(R.id.somethingreturn);
        yijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EReportActivity.this,YiJianfankui.class);
                startActivity(intent);
            }
        });
    }
}
