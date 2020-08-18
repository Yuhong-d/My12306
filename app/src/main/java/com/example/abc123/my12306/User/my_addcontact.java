package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Map;

public class my_addcontact extends AppCompatActivity {
    private ListView listView;
    private Button button;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addcontact);
        listView = findViewById(R.id.lv_myaddcontact);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        button=findViewById(R.id.btn_save);

        data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        final String attributes[] = {"姓名", "证件类型", "证件号码", "乘客类型", "电话"};
        String values[] = {""};
        for (int i = 0; i < attributes.length; i++) {
            map = new HashMap<String, Object>();
            map.put("name", attributes[i]);
            map.put("value",values[0]);
            data.add(map);
        }
        adapter = new SimpleAdapter(this, data, R.layout.my_detailcontact_item,
                new String[]{"name","value"},
                new int[]{R.id.attribute,R.id.value});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position){
                    case 0:
                        final EditText editName = new EditText(my_addcontact.this);
                        editName.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(my_addcontact.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入姓名")
                                .setView(editName)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int witch) {
                                        String Name = editName.getText().toString();
                                        if(TextUtils.isEmpty(Name)){
                                            DialogUtils.setClosable(dialog,false);
                                            editName.setError("请输入姓名");
                                            editName.requestFocus();
                                        }else{
                                            DialogUtils.setClosable(dialog,true);
                                            data.get(position).put("value",Name);
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
                    case 1:
                        final String[] idtype = {"身份证", "学生证", "残疾证", "其他"};
                        new AlertDialog.Builder(my_addcontact.this)
                                .setTitle("请选择证件类型")
                                .setSingleChoiceItems(idtype, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        String type = idtype[is];
                                        data.get(position).put("value",type);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

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
                    case 2:
                        final EditText editIdcard = new EditText(my_addcontact.this);
                        editIdcard.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(my_addcontact.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入证件号码")
                                .setView(editIdcard)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int witch) {
                                        String Idcard = editIdcard.getText().toString();
                                        if(TextUtils.isEmpty(Idcard)){
                                            DialogUtils.setClosable(dialog,false);
                                            editIdcard.setError("请输入证件号码");
                                            editIdcard.requestFocus();
                                        }else{
                                            DialogUtils.setClosable(dialog,true);
                                            data.get(position).put("value",Idcard);
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
                    case 3:
                        final String[] data1 = {"成人", "学生", "儿童", "特殊人群"};
                        new AlertDialog.Builder(my_addcontact.this)
                                .setTitle("请选择乘客类型")
                                .setSingleChoiceItems(data1, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        String type = data1[is];
                                        data.get(position).put("value",type);
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
                    case 4:
                        final EditText editTel = new EditText(my_addcontact.this);
                        editTel.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(my_addcontact.this)
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
                                            data.get(position).put("value",newTel);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
  /*  public void nameDialog(){
        final EditText editTel = new EditText(this);
        editTel.setText((String) data.get(position).get("value"));
        new AlertDialog.Builder(MyAccountActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("请输入电话号码")
                .setView(editTel)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int witch) {
                        String newTel = editTel.getText().toString();
                        if(TextUtils.isEmpty(newTel)){
                            nameDialog().setClosable(dialog,false);
                            editTel.setError("请输入电话号码");
                            editTel.requestFocus();
                        }else{
                            DialogUtils.setClosable(dialog,true);
                            data.get(i).put("value",newTel);
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
    }

   */

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.my_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //点击后返回
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
   /*     data =new ArrayList<>();
        Map<String,Object> row1 = new HashMap<>();
        row1.put("name", "姓名");
        data.add(row1);
        Map<String,Object> row2 = new HashMap<>();
        row2.put("idCardtype", "证件类型");
        data.add(row2);
        Map<String,Object> row3 = new HashMap<>();
        row3.put("idcard", "证件号吗");
        data.add(row3);
        Map<String,Object> row4 = new HashMap<>();
        row4.put("passeagertype", "乘客类型");
        data.add(row4);
        Map<String,Object> row5 = new HashMap<>();
        row5.put("tel", "电话");
        data.add(row5);
        adapter = new SimpleAdapter(this,data,R.layout.account_list_item_addcontact,new String[]{"name","idCardtype","idcard","passeagertype","tel"},
                new int[]{ R.id.attribute});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //每一行都要弹对话框
            }
        });
        }

    */

