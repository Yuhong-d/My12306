package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.abc123.my12306.R;

import java.io.Serializable;
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
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.Lv_detailcontact);
        data =new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
         final String[] name= {"张三(成人)","李四(学生)","王五(成人)"};
         final String[] idcard = {"身份证:234567349098675626","身份证:500289492648289327","身份证:623498100932456789"};
         final String[] num = {"电话:15239384456","电话:14589356245","电话:19858734562"};
        for (int i = 0; i < name.length; i++) {
            map = new HashMap<String, Object>();
            map.put("name", name[i]);
            map.put("idcard",idcard[i]);
            map.put("num", num[i]);
            data.add(map);
        }
        adapter = new SimpleAdapter(this,data,R.layout.account_list_item,new String[]{"name","idcard","num"},
                new int[]{ R.id.tvNameContact,  R.id.tvIdCardContact, R.id.tvTelContact });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(my_contact.this,my_detailcontact.class);
                intent.putExtra("info", (Serializable) data.get(position));
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //点击后返回
            case android.R.id.home:
                finish();
                break;
               // return true;
            case R.id.add:
                //跳转到添加页面
                Intent intent = new Intent(my_contact.this,my_addcontact.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
