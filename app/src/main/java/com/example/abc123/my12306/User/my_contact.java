package com.example.abc123.my12306.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_contact extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        listView = findViewById(R.id.Lv_detailcontact);
        data =new ArrayList<>();
        Map<String,Object> row = new HashMap<>();
        row.put("name", "东方人(成人)");
        row.put("idCard", "身份证:110110199009091111");
        row.put("tel", "电话:138888888899");
        data.add(row);
        adapter = new SimpleAdapter(this,data,R.layout.ticket_addusr_list_item,new String[]{"name","idCard","tel"},
                new int[]{ R.id.tvName,  R.id.tvIdCard, R.id.tvTel});
        listView.setAdapter(adapter);
    }
}
