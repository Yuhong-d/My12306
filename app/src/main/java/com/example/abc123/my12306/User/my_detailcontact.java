package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class my_detailcontact extends AppCompatActivity {
    private ListView lv1,lv2;
    private SimpleAdapter adapter;
    private List<HashMap<String, String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detailcontact);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final String attributes[] = {"姓名", "证件类型", "证件号码", "乘客类型", "电话"};
        String values[] = {"东方人", "身份证", "110110199009091111", "成人", "138888888899"};

        lv1 = findViewById(R.id.lv1);
        lv2=findViewById(R.id.lv2);
        listCreate(attributes, values, lv1, R.layout.activity_my_contact);
        listCreate(attributes,values,lv1,R.layout.detailcontact_list_item);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (attributes[position]){
                    case "乘客类型":
                        multiDialog();
                        break;
                    case "电话":
                        layDialog();
                        break;
                    case "姓名":
                        AlterDialog();
                        break;
                }
            }
        });
    }
    private void multiDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(my_detailcontact.this);
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
                    Toast.makeText(my_detailcontact.this,"您取消了选择",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(my_detailcontact.this,"您选择了"+str,Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void layDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(my_detailcontact.this);
        //创建view，并将布局加入view中
        View view= LayoutInflater.from(my_detailcontact.this).inflate(R.layout.number_dialog,null,false);
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
                Toast.makeText(my_detailcontact.this, "添加成功", Toast.LENGTH_SHORT).show();
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
    private void AlterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(my_detailcontact.this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(my_detailcontact.this).inflate(
                R.layout.dialog_edt, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final EditText edt = (EditText) view.findViewById(R.id.edt);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String str = edt.getText() == null ? "" : edt.getText()
                        .toString();
                Toast.makeText(my_detailcontact.this,str,Toast.LENGTH_SHORT).show();
            }
        });
        // 取消按钮
        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void listCreate( String attributes[],String values[],ListView lv,int resource){
        datalist=new ArrayList<HashMap<String, String>>();
        HashMap<String,String>map;
        for (int i=0;i<attributes.length;i++) {
            map = new HashMap<String, String>();
            map.put("attribute", attributes[i]);
            map.put("value", values[i]);
            datalist.add(map);
        }
        adapter=new MyAdapter(my_detailcontact.this,datalist,resource,
                new String[]{"attribute","value"},
                new int[]{R.id.attribute,R.id.value});
        lv.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.my_menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //点击后返回
            case android.R.id.home:
                finish();
            case R.id.delect:
                //删除此联系人
        }
        return super.onOptionsItemSelected(item);
    }
}
