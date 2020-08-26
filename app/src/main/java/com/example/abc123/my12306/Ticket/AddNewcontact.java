package com.example.abc123.my12306.Ticket;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.DialogUtils;
import com.example.abc123.my12306.User.my_contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNewcontact extends AppCompatActivity {
    private static final String TAG = "addContact";
    private ListView listView;
    private Button button;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> data;
    private SharedPreferences sp;
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "msg的数据： "+msg);
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String result = msg.obj.toString();
                   // Log.d(TAG, "result的数据： "+result);
               //     String a = "1";
                    if ("1".equals(msg.obj)){
                        Toast.makeText(AddNewcontact.this,"保存成功！",Toast.LENGTH_SHORT).show();
                        AddUserInfo.instance.finish();
                        Intent intent=new Intent(AddNewcontact.this,AddUserInfo.class);
                        startActivity(intent);
                        AddNewcontact.this.finish();

                    }else {
                        Toast.makeText(AddNewcontact.this,"保存失败！",Toast.LENGTH_SHORT).show();
                        AddNewcontact.this.finish();
                    }
                    break;
                case 2:
                    Toast.makeText(AddNewcontact.this,"数据错误！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


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
                        final EditText editName = new EditText(AddNewcontact.this);
                        editName.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(AddNewcontact.this)
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
                        final String[] idtype = {"身份证", "其他"};
                        new AlertDialog.Builder(AddNewcontact.this)
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
                        final EditText editIdcard = new EditText(AddNewcontact.this);
                        editIdcard.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(AddNewcontact.this)
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
                        final String[] data1 = {"成人", "学生"};
                        new AlertDialog.Builder(AddNewcontact.this)
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
                        final EditText editTel = new EditText(AddNewcontact.this);
                        editTel.setText((String) data.get(position).get("value"));
                        new AlertDialog.Builder(AddNewcontact.this)
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
                if (isContentEmpty()) {
                    Toast.makeText(AddNewcontact.this, "请将信息填写完整！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Commited();//是否同意保存

            }
        });
    }
    //判断内容是否填写
    private boolean isContentEmpty(){
        for (int i=0;i<data.size();i++){
            Log.d(TAG, "isContentEmpty: "+data.get(i).get("value").toString());
            if (data.get(i).get("value").toString().equals("")){
                return true;
            }
        }
        return false;
    }
    //判断是否加入
    private void Commited(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewcontact.this);
        builder.setTitle("提醒");
        builder.setMessage("是否保存到常用联系人");
        builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddNewcontact.this, AddUserInfo.class);
                    intent.putExtra("data", (Serializable) data);
                    setResult(RESULT_OK, intent);
                    finish();
                    dialog.dismiss();
            }

        });
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!NetUtils.check(AddNewcontact.this)) {
                    Toast.makeText(AddNewcontact.this, "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        // super.run();
                        Message msg = handler.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        String sessionId = sp.getString("cookie", "");
                        Log.d(TAG, "session： " + sessionId);
                        //建立请求
                        RequestBody requestBody = new FormBody.Builder()
                                .add("姓名", data.get(0).get("value").toString())
                                .add("证件类型", data.get(1).get("value").toString())
                                .add("证件号码", data.get(2).get("value").toString())
                                .add("乘客类型", data.get(3).get("value").toString())
                                .add("电话", data.get(4).get("value").toString())
                                .add("action", "new")
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Passenger")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responsedata = response.body().string();
                            Log.d(TAG, "获取的服务器数据： " + responsedata);
                            if (response.isSuccessful()) {

                                //    JSONObject jsonObject=new JSONObject(responsedata);
                                Gson gson = new GsonBuilder().create();
                                String resultString = gson.fromJson(responsedata, String.class);
                                msg.obj = resultString;
                                msg.what = 1;
                            } else {
                                msg.what = 2;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.what = 2;
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            msg.what = 3;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
        builder.create().show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(404);
        return super.onKeyDown(keyCode, event);
    }

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
                setResult(404);
                finish();
                break;
            case R.id.addcontact:
                addLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addLayout() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewcontact.this,new String[] {Manifest.permission.READ_CONTACTS},1);
            return;
        }
        ContentResolver cr = getContentResolver();
        //获取联系人,ContactsContract.Contacts.CONTENT_URI,
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{"_id", "display_name"}, null, null, null);
        String display_name = null;
        int _id = 0;
        while (c.moveToNext()) {
             _id = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
            display_name = c.getString(c.getColumnIndex("display_name"));
            Log.d("Addcontact", _id + "," + display_name);
        }
        c.close();
        //获取联系人号码
        Cursor c2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{_id+""}, null);
        String number = null;
        while (c2.moveToNext()) {
            number = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d(TAG, "addLayout: "+number);
        }
        c2.close();
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewcontact.this)
                .setNegativeButton("取消", null);
        View view = LayoutInflater.from(AddNewcontact.this).inflate(R.layout.addcontact_dialog, null, false);
        //将view加入builder
        builder.setView(view).setTitle("请选择");
        //创建dialog
        final Dialog dialog = builder.create();
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(display_name + "(" + number + ")");
        final String finalDisplay_name = display_name;
        final String finalNumber = number;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.setClosable(dialog, true);
                data.get(0).put("value", finalDisplay_name);
                data.get(4).put("value", finalNumber);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

        });
        dialog.show();
    }
    }

