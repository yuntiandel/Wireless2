package com.example.wireless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import bean.SerializableMap;


public class SingleEventActivity extends AppCompatActivity {

    private TextView number,expression,place,long_and_lati,submit_time,standard,deadline,note;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        initView();
        initData();

    }

    //初始化视图控件

    private void initView (){
        number = (TextView)findViewById(R.id.number);
        expression = (TextView)findViewById(R.id.expression);
        place = (TextView)findViewById(R.id.place);
        long_and_lati = (TextView)findViewById(R.id.long_and_lati);
        submit_time = (TextView)findViewById(R.id.submit_time);
        standard = (TextView)findViewById(R.id.standard);
        deadline = (TextView)findViewById(R.id.deadline);
        note = (TextView)findViewById(R.id.note);
        picture = (ImageView) findViewById(R.id.picture);
    }

    private void initData(){
        Bundle bundle = getIntent().getBundleExtra("every");
        SerializableMap serializableMap = (SerializableMap) bundle.get("event");
        Map<String,Object> map = new HashMap<>();
        map = serializableMap.getMap();

        number.setText(map.get("number").toString());
        expression.setText(map.get("expression").toString());
        place.setText(map.get("place").toString());
        long_and_lati.setText(map.get("long_and_lati").toString());
        submit_time.setText(map.get("submit_time").toString());
        standard.setText(map.get("standard").toString());
        deadline.setText(map.get("deadline").toString());
        note.setText(map.get("note").toString());
        picture.setImageBitmap(null);
    }


}
