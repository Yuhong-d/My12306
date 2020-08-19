package com.example.abc123.my12306.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
//        listCreate(attributes, values, ls1, R.layout.ticket_addusr_list_item);
        listCreate(attributes,values,ls1,R.layout.account_list_item_2);
        ls1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (attributes[position]){
                    case "乘客类型":
                        multiDialog();
                        break;
                    case "电话":
                        layDialog();
                        break;
                }
            }
        });
    }
    //多选框，待修正
    private void multiDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(my_account.this);
        builder.setTitle("请选择：");
        final String[] items={"成人","学生","儿童","特殊人群"};
        final boolean[] cheackedIetms={true,false,false,false};
        builder.setMultiChoiceItems(items, cheackedIetms, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    cheackedIetms[which]=true;
                }else {
                    cheackedIetms[which]=false;
                    Toast.makeText(my_account.this,"您取消了选择",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str="";
                for (int i=0;i<cheackedIetms.length;i++){
                    if (cheackedIetms[i]){
                        str=str+items[i];
                    }
                }
                Toast.makeText(my_account.this,"您选择了"+str,Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

        private void layDialog(){
            AlertDialog.Builder builder=new AlertDialog.Builder(my_account.this);
            //创建view，并将布局加入view中
            View view= LayoutInflater.from(my_account.this).inflate(R.layout.number_dialog,null,false);
            //将view加入builder
            builder.setView(view).setTitle("请输入电话号码：");
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
                    String str = edt_number.getText().toString();
                   // value.setText(str);
                        Toast.makeText(my_account.this, "添加成功", Toast.LENGTH_SHORT).show();
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
