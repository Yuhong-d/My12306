package com.example.abc123.my12306.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        Map<String,Object> row1 = new HashMap<>();
        row1.put("name", "姓名");
        data.add(row1);
        Map<String,Object> row2 = new HashMap<>();
        row2.put("idCardtype", "证件类型");
        data.add(row2);
        Map<String,Object> row3 = new HashMap<>();
        row3.put("idcard", "证件号吗");
        data.add(row3);
        Map<String,Object> row4 = new HashMap<>();
        row4.put("passeagertype", "乘客类型");
        data.add(row4);
        Map<String,Object> row5 = new HashMap<>();
        row5.put("tel", "电话");
        data.add(row5);
        adapter = new SimpleAdapter(this,data,R.layout.account_list_item,new String[]{"name"},
                new int[]{ R.id.tvNameContact});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //每一行都要弹对话框
            }
        });
        }
}