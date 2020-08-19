package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserInfo extends AppCompatActivity {

    private ListView listView;
    private Button btn;//未初始化未设置点击事件
    private List<Map<String,String>> data;
    private AddUsrInfoAdapter addUsrInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_add_user);
        listView=findViewById(R.id.ticket_addusr_ls);
        Map<String,String>map=new HashMap<>();
        map.put("name","冬不拉");
        map.put("idcard",1111111111+"");
        map.put("number","1234343242");
        data=new ArrayList<>();
        data.add(map);
        addUsrInfoAdapter=new AddUsrInfoAdapter(AddUserInfo.this,data);
        listView.setAdapter(addUsrInfoAdapter);
        
    }
}
