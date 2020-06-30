package com.example.abc123.my12306.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ListView listView;
    // 定义数组
    private String[] data = {"我的联系人","我的账户","我的密码"};
    List<Map<String, Object>> dataList;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        listView =  findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserActivity.this, R.layout.my_item,R.id.textView2, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent=new Intent();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (data[position]){
                    case "我的联系人":
                        intent.setClass(UserActivity.this,my_contact.class);
                        startActivity(intent);
                    case "我的账户":
                        intent.setClass(UserActivity.this,my_account.class);
                        startActivity(intent);
                    case "我的密码":
                        intent.setClass(UserActivity.this,my_contact.class);
                        startActivity(intent);
                }
            }
        });

    }
}
