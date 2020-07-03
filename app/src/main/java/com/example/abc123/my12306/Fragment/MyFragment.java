package com.example.abc123.my12306.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.UserActivity;
import com.example.abc123.my12306.User.my_account;
import com.example.abc123.my12306.User.my_contact;
import com.example.abc123.my12306.User.my_pwd;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class MyFragment extends Fragment {

    private ListView listView;
    // 定义数组
    private String[] data = {"我的联系人","我的账户","我的密码"};
    List<Map<String, Object>> dataList;

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView =  view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_item,R.id.textView2, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent=new Intent();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (data[position]){
                    case "我的联系人":
                        intent.setClass(getActivity(), my_contact.class);
                        startActivity(intent);
                        break;
                    case "我的账户":
                        intent.setClass(getActivity(), my_account.class);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity() );
        //创建view，并将布局加入view中
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.number_dialog,null,false);
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
                intent.setClass(getActivity(), my_pwd.class);
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
