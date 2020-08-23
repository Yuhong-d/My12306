package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.abc123.my12306.util.Constant;
import com.example.abc123.my12306.util.NetUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class my_detailcontact extends AppCompatActivity {
    private static final String TAG = "detailcontact";
    private ListView detailcontact;
    private Button button;
    private SharedPreferences sp;
    private SimpleAdapter adapter;
    private String action = "";
    private List<Map<String,Object>> data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String result = msg.obj.toString();
            switch (msg.what){
                case 1:
                    if ("1".equals(result)){
                        Toast.makeText(my_detailcontact.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        my_detailcontact.this.finish();

                    }else if ("-1".equals(result)){
                        Toast.makeText(my_detailcontact.this,"修改失败！",Toast.LENGTH_SHORT).show();
                        my_detailcontact.this.finish();
                    }
                    break;
                case 2:
                    Toast.makeText(my_detailcontact.this,"服务器错误，请重试！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(my_detailcontact.this,"请重新登录！",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if ("1".equals(result)){
                        Toast.makeText(my_detailcontact.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        my_detailcontact.this.finish();

                    }else if ("-1".equals(result)){
                        Toast.makeText(my_detailcontact.this,"删除失败！",Toast.LENGTH_SHORT).show();
                        my_detailcontact.this.finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detailcontact);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        detailcontact=findViewById(R.id.detailcontact);
        button=findViewById(R.id.btn_myContact);
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
        map3.put("key2",idtype.split("\\:")[0]);
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
        map5.put("key2",tel.split("\\：")[0]);
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
                new Thread(){
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        action = "update";
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        String sessionId =sp.getString("cookie","");
                        Log.d(TAG, "session： " + sessionId);
                        //建立请求
                        RequestBody requestBody=new FormBody.Builder()
                                .add("姓名",data.get(0).get("key2").toString())
                                .add("证件类型",data.get(1).get("key2").toString())
                                .add("证件号码",data.get(2).get("key2").toString())
                                .add("乘客类型",data.get(3).get("key2").toString())
                                .add("电话",data.get(4).get("key2").toString())
                                .add("action",action)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Passenger")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responsedata = response.body().string();
                            Log.d(TAG, "获取的服务器数据： "+responsedata);
                            if (response.isSuccessful()) {
                                //    JSONObject jsonObject=new JSONObject(responsedata);
                                Gson gson = new GsonBuilder().create();
                                String resultString = gson.fromJson(responsedata, String.class);
                                msg.obj = resultString;
                                msg.what = 1;
                            }else{
                                msg.what=2;
                            }
                        }catch (IOException e) {
                            e.printStackTrace();
                            msg.what=2;
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            msg.what=3;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(my_detailcontact.this);
                builder.setTitle("提醒！");
                builder.setMessage("确定要删除该联系人吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!NetUtils.check(my_detailcontact.this)){
                            Toast.makeText(my_detailcontact.this,"当前网络不可用",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                Message message = handler.obtainMessage();
                                action = "remove";
                                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                String sessionID = sharedPreferences.getString("cookie", "");
                                try {
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("姓名", data.get(0).get("key2").toString())
                                            .add("证件类型", data.get(1).get("key2").toString())
                                            .add("证件号码", data.get(2).get("key2").toString())
                                            .add("乘客类型", data.get(3).get("key2").toString())
                                            .add("电话", data.get(4).get("key2").toString())
                                            .add("action", action)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("http://192.168.1.3:8080/My12306/otn/Passenger")
                                            .addHeader("cookie", sessionID)
                                            .post(requestBody)
                                            .build();
                                    Response response = okHttpClient.newCall(request).execute();
                                    String responseData = response.body().string();
                                    if (response.isSuccessful()) {
                                        Gson gson = new GsonBuilder().create();
                                        String resultString = gson.fromJson(responseData, String.class);
                                        message.what = 4;
                                        message.obj = resultString;
                                    }
                                    else {
                                        message.what = 2;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    message.what = 2;
                                } catch (JsonSyntaxException e){
                                    e.printStackTrace();
                                    message.what = 3;//请重新登录
                                }
                                handler.sendMessage(message);
                            }
                        }.start();
                        //删除联系人并退出
                        my_detailcontact.this.finish();
                    }
                });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
