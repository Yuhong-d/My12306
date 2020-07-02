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
        Map<String,Object> row = new HashMap<>();
        row.put("name", "东方人(成人)");
        row.put("idCard", "身份证:110110199009091111");
        row.put("tel", "电话:138888888899");
        data.add(row);
        adapter = new SimpleAdapter(this,data,R.layout.ticket_addusr_list_item,new String[]{"name","idCard","tel"},
                new int[]{ R.id.tvName,  R.id.tvIdCard, R.id.tvTel});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(my_contact.this,my_detailcontact.class);
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
            case R.id.add:
                //跳转到添加页面
                Intent intent = new Intent();
                intent.setClass(this,my_addcontact.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
