package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abc123.my12306.Fragment.TicketFragment;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.my_addcontact;
import com.example.abc123.my12306.User.my_contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserInfo extends AppCompatActivity {
    public static AddUserInfo instance = null;
    private ListView listView;
    private Button btn;//未初始化未设置点击事件
    private List<Map<String,Object>> data;
    private AddUsrInfoAdapter addUsrInfoAdapter;
    List<Integer> listItemId=new ArrayList<Integer>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    addUsrInfoAdapter=new AddUsrInfoAdapter(AddUserInfo.this,data);
                    listView.setAdapter(addUsrInfoAdapter);
                    break;
                case 2:
                    Toast.makeText(AddUserInfo.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_add_user);
        listView=findViewById(R.id.ticket_addusr_ls);
        btn=findViewById(R.id.buttonAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemId.clear();
                List<Map<String,Object>> newData=new ArrayList<>();
                if (!addUsrInfoAdapter.mchecked.get(0)){
                    Map<String,Object> map=data.get(0);
                    Map<String,Object>map1=new HashMap<>();
                    map1.put("name", map.get("name"));
                    map1.put("idcard",map.get("idcard"));
                    map1.put("num",map.get("num"));
                    newData.add(map);
                }
                for (int i=0;i<addUsrInfoAdapter.mchecked.size();i++){
                    if (addUsrInfoAdapter.mchecked.get(i)){
                        listItemId.add(i);
                            Map<String,Object> map=data.get(i);
                            Map<String,Object>map1=new HashMap<>();
                            map1.put("name", map.get("name"));
                            map1.put("idcard",map.get("idcard"));
                            map1.put("num",map.get("num"));
                            newData.add(map);
                    }
                    }


                    if (listItemId.size()==0){
                    Toast.makeText(AddUserInfo.this,"未选择乘车人",Toast.LENGTH_SHORT).show();
                }
                    else {
                        Intent intent=new Intent(AddUserInfo.this,TicketBookingActivity.class);
                        intent.putExtra("data", (Serializable) newData);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg = handler.obtainMessage();
                OkHttpClient client = new OkHttpClient();
                //获取sessionId
                SharedPreferences sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                String sessionId =sp.getString("cookie","");
                //建立请求
//                RequestBody requestBody=new FormBody.Builder()
//                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/My12306/otn/TicketPassengerList")
                        .addHeader("cookie", sessionId)
//                        .post(requestBody)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responsedata = response.body().string();
                        JSONArray jsonArray = new JSONArray(responsedata);
                        Map<String, Object> map;
                        data=new ArrayList<>();
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
        
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(404);
        return super.onKeyDown(keyCode, event);
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
                Intent intent = new Intent(AddUserInfo.this, AddNewcontact.class);
                startActivityForResult(intent,110);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent d) {
        super.onActivityResult(requestCode, resultCode, d);
        if (resultCode==404){
            Toast.makeText(AddUserInfo.this,"没有添加联系人",Toast.LENGTH_SHORT);
            return;
        }
        switch (requestCode){
            case 110:
                List<HashMap<String, Object>> newdata= (List<HashMap<String, Object>>) d.getSerializableExtra("data");
                Map<String,Object>map = new HashMap<String, Object>();
                String type=newdata.get(3).get("value").toString();
                String idType=newdata.get(1).get("value").toString();
                map.put("name", newdata.get(0).get("value").toString()+"("+type+")");
                map.put("idcard",idType+"："+newdata.get(2).get("value").toString());
                map.put("num", "电话："+newdata.get(4).get("value").toString());
                data.add(map);
                addUsrInfoAdapter.notifyDataSetChanged();
                break;
        }
    }
}
