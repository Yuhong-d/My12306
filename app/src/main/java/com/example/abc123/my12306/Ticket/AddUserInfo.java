package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.abc123.my12306.Fragment.TicketFragment;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.Passenger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class AddUserInfo extends AppCompatActivity {

    private ListView listView;
    private Button btn;//未初始化未设置点击事件
    private List<Map<String,Object>> data;
    private AddUsrInfoAdapter addUsrInfoAdapter;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> contactData;
    List<Integer> listItemId=new ArrayList<Integer>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Passenger[] passengers = (Passenger[]) msg.obj;
                    data  = new ArrayList<>();
                    contactData = new ArrayList<>();
                    for (Passenger passenger:passengers){
                        Map<String,Object> map = new HashMap<>();
                        //map.put("name","东方人(成人)");
                        map.put("name",passenger.getName()+"("+passenger.getType()+")");
                        // map.put("idCard",IdCard[i]);
                        map.put("idCard",passenger.getIdType()+":"+passenger.getId());
                        // map.put("tel",Tel[i]);
                        map.put("tel","电话:"+passenger.getTel());
                        map.put("passenger",passenger);
                        data.add(map);
                    }
                    adapter = new SimpleAdapter(
                            AddUserInfo.this,
                            data,
                            R.layout.item_ticket_passenger_list_step3,
                            new String[]{"name","idCard","tel"},
                            new int[]{R.id.tvTicketPassengerListStep3Name,R.id.tvTicketPassengerListStep3IdCard,R.id.tvTicketPassengerListStep3Tel})
                    {
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            final int posi = position;
                            final View view = super.getView(position, convertView, parent);
                            CheckBox checkBox = view.findViewById(R.id.ckTicketPassengerListStep3);
                            //TODO 对复选框实现点击监听
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        contactData.add(data.get(posi));
                                    }else {
                                        contactData.remove(posi);
                                    }
                                }
                            });
                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
                    break;
                case 2:
                    Toast.makeText(AddUserInfo.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_add_user);
        listView=findViewById(R.id.ticket_addusr_ls);
        btn=findViewById(R.id.buttonAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemId.clear();
                Intent intent = new Intent();
                intent.putExtra("contactdata", (Serializable) contactData);
                //startActivity(intent);
                setResult(RESULT_OK,intent);
                finish();
/*
                List<Map<String,Object>> newData=new ArrayList<>();
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
 */
                }
        });
//        Map<String,String>map=new HashMap<>();
//        map.put("name","冬不拉");
//        map.put("idcard",1111111111+"");
//        map.put("number","1234343242");
//        data=new ArrayList<>();
//        data.add(map);
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
                        /*
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

                         */
                        Gson gson = new GsonBuilder().create();
                        Passenger[] passengers = gson.fromJson(responsedata,Passenger[].class);

                        msg.what = 1;
                        msg.obj = passengers;
                      //  in.close();
                    }
                    else{
                        msg.what=2;
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    msg.what=2;
                } catch (JsonSyntaxException e) {
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
}
