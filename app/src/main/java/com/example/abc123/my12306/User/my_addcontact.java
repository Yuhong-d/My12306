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

public class my_addcontact extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addcontact);
        listView = findViewById(R.id.Lv_detailcontact);
        data =new ArrayList<>();
        Map<String,Object> row = new HashMap<>();
        row.put("name", "姓名");
        data.add(row);
        adapter = new SimpleAdapter(this,data,R.layout.account_list_item,new String[]{"name"},
                new int[]{ R.id.tvNameContact});
        listView.setAdapter(adapter);
        }
}
