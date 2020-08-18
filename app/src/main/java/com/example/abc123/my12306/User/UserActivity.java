package com.example.abc123.my12306.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ListView listView;
    // 定义数组
    private String[] data = {"我的联系人","我的账户","我的密码"};
    List<Map<String, Object>> dataList;
//此页面已被移动至MyFragment，此页待作废
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        listView =  findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserActivity.this, R.layout.my_item,R.id.texttime, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent=new Intent();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (data[position]){
                    case "我的联系人":
                        intent.setClass(UserActivity.this,my_contact.class);
                        startActivity(intent);
                        break;
                    case "我的账户":
                        intent.setClass(UserActivity.this,my_account.class);
                        startActivity(intent);
                        break;
                    case "我的密码":
                       layDialog();
                        break;
                }
            }
        });

    }
    private void layDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);
        //创建view，并将布局加入view中
        View view= LayoutInflater.from(UserActivity.this).inflate(R.layout.number_dialog,null,false);
        //将view加入builder
        builder.setView(view).setTitle("请输入原密码：");
        //创建dialog
        final Dialog dialog=builder.create();
        //初始化控件
        final EditText edt_number=(EditText) view.findViewById(R.id.edt_number);
        Button confirm=(Button)view.findViewById(R.id.btnyes);
        Button cancel=(Button)view.findViewById(R.id.btnno);
        //设置Button的事件和内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(UserActivity.this,my_pwd.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
