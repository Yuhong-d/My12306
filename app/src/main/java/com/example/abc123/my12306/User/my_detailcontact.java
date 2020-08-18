package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.abc123.my12306.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_detailcontact extends AppCompatActivity {
    private ListView detailcontact;
    private Button button;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detailcontact);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        detailcontact=findViewById(R.id.detailcontact);
        button=findViewById(R.id.btn_myContact);
//        final String attributes[] = {"姓名", "证件类型", "证件号码", "乘客类型", "电话"};
//        String values[] = {"东方人", "身份证", "110110199009091111", "成人", "138888888899"};

//        lv1 = findViewById(R.id.lv1);
//        lv2=findViewById(R.id.lv2);
 //       listCreate(attributes, values, lv1, R.layout.activity_my_contact);
//        listCreate(attributes,values,lv1,R.layout.detailcontact_list_item);
        /*   private void listCreate( String attributes[],String values[],ListView lv,int resource){
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

  */
        Map<String,Object> contact = (HashMap<String, Object>) getIntent().getSerializableExtra("info");

        data = new ArrayList<Map<String, Object>>();
        Map<String,Object> map1 = new HashMap<>();
        String name = (String) contact.get("name");
        map1.put("key1","姓名");
        //以左括号进行分割，取第一段
        map1.put("key2",name.split("\\(")[0]);
        map1.put("key3",R.drawable.forward_25);
        data.add(map1);

        Map<String,Object> map2 = new HashMap<>();
        String idtype = (String) contact.get("idcard");
        map2.put("key1","证件类型");
        //以冒号进行分割，取第一段
        map2.put("key2",idtype.split("\\:")[0]);
        map2.put("key3",R.drawable.flg_null);
        data.add(map2);

        Map<String,Object> map3 = new HashMap<>();
        //String idtype = (String) contact.get("idCard");
        map3.put("key1","证件号码");
        //以冒号进行分割，取第一段
        map3.put("key2",idtype.split("\\:")[1]);
        map3.put("key3",R.drawable.flg_null);
        data.add(map3);

        Map<String,Object> map4 = new HashMap<>();
        map4.put("key1","乘客类型");
        map4.put("key2",name.split("\\(")[1].split("\\)")[0]);
        map4.put("key3",R.drawable.forward_25);
        data.add(map4);

        Map<String,Object> map5 = new HashMap<>();
        String tel = (String) contact.get("num");
        map5.put("key1","电话");
        //以冒号进行分割，取第二段
        map5.put("key2",tel.split("\\:")[1]);
        map5.put("key3",R.drawable.forward_25);
        data.add(map5);

        adapter = new SimpleAdapter(
                this,
                data,
                R.layout.my_detailcontact_item,
                new String[]{"key1","key2","key3"},
                new int[]{R.id.attribute,R.id.value,R.id.img});

        detailcontact.setAdapter(adapter);



        detailcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position){
                    case 0:
                        final EditText editName = new EditText(my_detailcontact.this);
                        editName.setText((String) data.get(position).get("key2"));
                        new AlertDialog.Builder(my_detailcontact.this)
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
                                            data.get(position).put("key2",Name);
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
                        new AlertDialog.Builder(my_detailcontact.this)
                                .setTitle("请选择乘客类型")
                                .setSingleChoiceItems(data1, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        String type = data1[is];
                                        data.get(position).put("key2",type);
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
                        final EditText editTel = new EditText(my_detailcontact.this);
                        editTel.setText((String) data.get(position).get("key2"));
                        new AlertDialog.Builder(my_detailcontact.this)
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
                                            data.get(position).put("key2",newTel);
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
