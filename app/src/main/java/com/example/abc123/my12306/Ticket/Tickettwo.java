package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.adpter.Ticketadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tickettwo extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,Object>> list;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickettwo);
        listView = findViewById(R.id.lv_ticketBuy);
        list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("seattype","无座");
        map1.put("ticket","39张");
        map1.put("price","￥154.0");
        list.add(map1);
        Ticketadapter ticketadapter = new Ticketadapter(this,list);
        listView.setAdapter(ticketadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(Tickettwo.this,AddUserInfo.class);
                startActivity(intent);
            }
        });
        tv=findViewById(R.id.textView8);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Tickettwo.this,AddUserInfo.class);
                startActivity(intent);
            }
        });
    }
}
