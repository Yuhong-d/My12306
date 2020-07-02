package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.abc123.my12306.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.Tickettwo;

public class Ticketone extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketone);
        listView = findViewById(R.id.lv_ticket);
        list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("banci","D5");
        map1.put("tv_start","07:05");
        map1.put("tv_over","11:55(0日)");
        map1.put("seat1","无座:39");
        map1.put("seat2","硬座:38");
        map1.put("seat3","一等座:17");
        map1.put("seat4","软卧:48");
        list.add(map1);
        Tickettwo tickettwo = new Tickettwo(this,list);
        listView.setAdapter(tickettwo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(Ticketone.this, com.example.abc123.my12306.Ticket.Tickettwo.class);
                startActivity(intent);
            }
        });
    }
}
