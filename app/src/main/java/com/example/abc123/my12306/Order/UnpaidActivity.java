package com.example.abc123.my12306.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnpaidActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, String>> dataList;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        ls=findViewById(R.id.ls);
        tv=findViewById(R.id.tv_ordernum);
        Intent intent=getIntent();
        tv.setText(intent.getStringExtra("num"));
        dataList=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<>();
        map.put("name","冬不拉");
        map.put("train",intent.getStringExtra("train"));
        map.put("time",intent.getStringExtra("time"));
        map.put("carriage","2车19号");
        dataList.add(map);
        OrderActivityAdapter activityAdapter=new OrderActivityAdapter(UnpaidActivity.this,dataList);
        ls.setAdapter(activityAdapter);
    }
}
