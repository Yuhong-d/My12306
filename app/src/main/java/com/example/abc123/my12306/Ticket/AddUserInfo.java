package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;

public class AddUserInfo extends AppCompatActivity {

    private ListView listView;
    private Button btn;//未初始化未设置点击事件
    private List<Map<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_add_user);
        listView=findViewById(R.id.ticket_addusr_ls);
        
    }
}
