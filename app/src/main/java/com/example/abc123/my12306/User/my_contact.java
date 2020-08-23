package com.example.abc123.my12306.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_contact extends AppCompatActivity {
    private static final String TAG = "Contact";
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    adapter = new SimpleAdapter(my_contact.this,
                            data,
                            R.layout.account_list_item,
                            new String[]{"name","idcard","num"},
                            new int[]{ R.id.tvNameContact, R.id.tvIdCardContact, R.id.tvTelContact });
                    listView.setAdapter(adapter);
                    break;
                case 2:
                    Toast.makeText(my_contact.this,"数据错误！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        //返回按钮
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.Lv_detailcontact);
        data =new ArrayList<>();
        if (!NetUtils.check(my_contact.this)) {
            Toast.makeText(my_contact.this, "网络异常，请检查！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
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
//                RequestBody requestBody=new FormBody.Builder()
//                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/My12306/otn/PassengerList")
                        .addHeader("cookie", sessionId)
//                        .post(requestBody)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "response"+response);
                    if (response.isSuccessful()) {
                        String responsedata = response.body().string();
                        Log.d(TAG, "responseData"+responsedata);
                        JSONArray jsonArray = new JSONArray(responsedata);
                        Map<String, Object> map = new HashMap<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            map = new HashMap<String, Object>();
                            String type=obj.getString("type");
                            String idType=obj.getString("idType");
                            map.put("name", obj.get("name")+"("+type+")");
                            map.put("idcard",idType+"："+obj.get("id"));
                            map.put("num", "电话："+obj.get("tel"));
                            data.add(map);
                        }
                        msg.what=1;
                    }
                    else{
                        msg.what=2;
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    msg.what=2;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what=2;
                }
                handler.sendMessage(msg);
            }
        }.start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(my_contact.this,my_detailcontact.class);
                intent.putExtra("info", (Serializable) data.get(position));
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //点击后返回
            case android.R.id.home:
                finish();
                break;
               // return true;
            case R.id.add:
                //跳转到添加页面
                Intent intent = new Intent(my_contact.this,my_addcontact.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
