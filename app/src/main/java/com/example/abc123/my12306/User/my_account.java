package com.example.abc123.my12306.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class my_account extends AppCompatActivity {
    private ListView ls1,ls2;
    private SimpleAdapter adapter;
    private List<HashMap<String,String>>datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        final String attributes[] = {"用户名", "姓名", "证件类型", "证件号码", "乘客类型", "电话"};
       String values[] = {"dong", "冬不拉", "身份证", "1111111111111", "成人", "13982763892"};

        ls1 = findViewById(R.id.ls1);
        listCreate(attributes,values,ls1,R.layout.account_list_item_2);

        ls1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (attributes[position]){
                    case "乘客类型":
                        final String[] data1 = {"成人", "学生", "儿童", "特殊人群"};
                        new AlertDialog.Builder(my_account.this)
                                .setTitle("请选择乘客类型")
                                .setSingleChoiceItems(data1, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        String type = data1[is];
                                        datalist.get(position).put("value",type);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();
                        break;
                    case "电话":
                        final EditText editTel = new EditText(my_account.this);
                        editTel.setText((String) datalist.get(position).get("value"));
                        new android.app.AlertDialog.Builder(my_account.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入电话号码")
                                .setView(editTel)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int witch) {
                                        String newTel = editTel.getText().toString();
                                        if(TextUtils.isEmpty(newTel)){
                                            DialogUtils.setClosable(dialog,false);
                                            editTel.setError("请输入电话号码");
                                            editTel.requestFocus();
                                        }else{
                                            DialogUtils.setClosable(dialog,true);
                                            datalist.get(position).put("value",newTel);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        DialogUtils.setClosable(dialog,true);
                                    }
                                })
                                .create()
                                .show();
                        break;
                }
            }
        });
    }

    private void listCreate( String attributes[],String values[],ListView ls,int resource){
        datalist=new ArrayList<HashMap<String, String>>();
        HashMap<String,String>map;
        for (int i=0;i<attributes.length;i++) {
            map = new HashMap<String, String>();
            map.put("attribute", attributes[i]);
            map.put("value", values[i]);
            datalist.add(map);
        }
        adapter=new MyAdapter(my_account.this,datalist,resource,
                new String[]{"attribute","value"},
                new int[]{R.id.attribute,R.id.value});
        ls.setAdapter(adapter);
    }
}
