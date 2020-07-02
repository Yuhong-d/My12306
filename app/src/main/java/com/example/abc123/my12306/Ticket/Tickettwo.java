package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import com.example.abc123.my12306.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.Ticketadapter;

public class Tickettwo extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,Object>> list;

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
    }
}
