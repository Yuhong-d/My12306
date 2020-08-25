package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.abc123.my12306.MainActivity;
import com.example.abc123.my12306.Md5Utils;
import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class my_account extends AppCompatActivity {
    private static final String TAG = "myaccount";
    private ListView ls;
    private Button btn;
    private SimpleAdapter adapter;
    private List<HashMap<String, String>> datalist;
    private ArrayList<String> values;
    private Handler handler,handler1;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        final String attributes[] = {"用户名", "姓名", "证件类型", "证件号码", "乘客类型", "电话"};
        ls = findViewById(R.id.ls1);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        listCreate(attributes, values, ls, R.layout.account_list_item_2);
                        break;
                    case 2:
                        Toast.makeText(my_account.this,"数据错误！",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //保存按钮事件
        handler1=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(my_account.this,"已保存",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(my_account.this,"保存失败！",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        if (!NetUtils.check(my_account.this)) {
            Toast.makeText(my_account.this, "网络异常，请检查！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = handler.obtainMessage();
                OkHttpClient client = new OkHttpClient();
                //获取sessionId
                sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                String sessionId =sp.getString("cookie","");
                Log.d(TAG, "session： " + sessionId);
                //建立请求
                RequestBody requestBody=new FormBody.Builder()
                        .add("action","query")
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/My12306/otn/Account")
                        .addHeader("cookie", sessionId) .post(requestBody).build();
                try {
                    //获取响应数据
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    Log.d(TAG, "run: "+responseData);
                    if (response.isSuccessful()) {
                        //解析数据，json
                        values=new ArrayList<>();
                        JSONObject object=new JSONObject(responseData);
                        values.add(object.getString("username"));
                        values.add(object.getString("name"));
                        values.add(object.getString("idType"));
                        values.add(object.getString("id"));
                        values.add(object.getString("type"));
                        values.add(object.getString("tel"));
                        msg.what=1;
                    }else {
                        msg.what=2;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what=2;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what=2;
                }
                handler.sendMessage(msg);
            }
        }.start();
        //listview的点击事件
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (attributes[position]) {
                    case "乘客类型":
                        final String[] data1 = {"成人", "学生", "儿童", "特殊人群"};
                        new AlertDialog.Builder(my_account.this)
                                .setTitle("请选择乘客类型")
                                .setSingleChoiceItems(data1, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int is) {
                                        String type = data1[is];
                                        datalist.get(position).put("value", type);
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
                                        if (TextUtils.isEmpty(newTel)) {
                                            DialogUtils.setClosable(dialog, false);
                                            editTel.setError("请输入电话号码");
                                            editTel.requestFocus();
                                        } else {
                                            DialogUtils.setClosable(dialog, true);
                                            datalist.get(position).put("value", newTel);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        DialogUtils.setClosable(dialog, true);
                                    }
                                })
                                .create()
                                .show();
                        break;
                }
            }
        });
        btn=findViewById(R.id.buttonSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetUtils.check(my_account.this)){
                    Toast.makeText(my_account.this, "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message msg2=handler1.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        String sessionId =sp.getString("cookie","");
                        //建立请求
                        RequestBody requestBody=new FormBody.Builder()
                                .add("乘客类型",datalist.get(4).get("value"))
                                .add("电话",datalist.get(5).get("value"))
                                .add("action","update")
                                .build();
                        Request request=new Request.Builder().
                                url("http://10.0.2.2:8080/My12306/otn/Account")
                                .header("cookie",sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            if (response.isSuccessful()){
                                String responsedata=response.body().string();
                                Log.d(TAG, "run: "+responsedata);
                                JSONObject object=new JSONObject(responsedata);
                                String type=object.getString("type");
                                String tel=object.getString("tel");
                                Log.d(TAG, "run: "+datalist.get(4).get("value"));
                                msg2.what=1;
                            }else {
                                msg2.what=2;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg2.what=2;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg2.what=2;
                        }
                        handler1.sendMessage(msg2);
                    }
                }.start();
            }
        });
    }
    //
    private void listCreate(String attributes[], ArrayList<String> values, ListView ls, int resource) {
                datalist = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;
                for (int i = 0; i < attributes.length; i++) {
                    map = new HashMap<String, String>();
                    map.put("attribute", attributes[i]);
                    map.put("value", values.get(i));
                    datalist.add(map);
                }
                adapter = new MyAdapter(my_account.this, datalist, resource,
                        new String[]{"attribute", "value"},
                        new int[]{R.id.attribute, R.id.value});
                ls.setAdapter(adapter);
            }
}
