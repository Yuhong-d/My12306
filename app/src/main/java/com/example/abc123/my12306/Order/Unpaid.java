package com.example.abc123.my12306.Order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Unpaid extends Fragment {
    private ListView ls;
    private List<Map<String, Object>> dataList,transdata;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    OrderAdapter orderAdapter=new OrderAdapter(getContext(),dataList);
                    ls.setAdapter(orderAdapter);
                    break;
                case 2:
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public Unpaid(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_layout_unpaid,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ls=view.findViewById(R.id.ls);
//        dataList=new ArrayList<Map<String, String>>();
//        Map<String,String> map=new HashMap<>();
//        map.put("num","2014yH");
//        map.put("type","待支付");
//        map.put("train","G011");
//        map.put("time","2016-4-5");
//        map.put("end","北京->上海");
//        map.put("person","1人");
//        map.put("price","258.0");
//        dataList.add(map);
//        OrderAdapter orderAdapter=new OrderAdapter(getContext(),dataList);
////        ls.setAdapter(orderAdapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //数据请求
        if (!NetUtils.check(getContext())) {
            Toast.makeText(getContext(), "网络异常，请检查！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg=handler.obtainMessage();
                OkHttpClient client = new OkHttpClient();
                //获取sessionId
                SharedPreferences sp=getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                String sessionId =sp.getString("cookie","");
                RequestBody requestBody=new FormBody.Builder()
                        .add("status",0+"")
                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/My12306/otn/OrderList")
                        .addHeader("cookie", sessionId)
                        .post(requestBody)
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    if (response.isSuccessful()){
                        String responsedata=response.body().string();
                        Log.d("Unpaid", "responsedata: "+responsedata);
                        //解析数据，json
                        dataList=new ArrayList<>();//查询结果
                        transdata=new ArrayList<>();
                        JSONArray jsonArray1=new JSONArray(responsedata);
                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject obj1= (JSONObject) jsonArray1.get(i);
                            Map<String,Object> map=new HashMap<>();
                            map.put("num",obj1.getString("id"));
                            map.put("type","未支付");
                            JSONObject object1=obj1.getJSONObject("train");
                            map.put("train",object1.getString("trainNo"));
                            map.put("time",object1.getString("startTrainDate"));
                            object1.getString("fromStationName");
                            map.put("end", object1.getString("fromStationName")+"->"+object1.getString("toStationName"));
                            JSONArray jsonArray2=obj1.getJSONArray("passengerList");
                            map.put("person",jsonArray2.length()+"");
                            map.put("price",obj1.getString("orderPrice"));
                            transdata=new ArrayList<>();
                            Map<String,Object> map1 = null;
                            for (int j=0;j<jsonArray2.length();j++){
                                map1=new HashMap<>();
                                JSONObject obj2=jsonArray2.getJSONObject(j);
                                map1.put("name",obj2.getString("name"));
                                map1.put("train",object1.getString("trainNo"));
                                map1.put("time",object1.getString("startTrainDate"));
                                map1.put("carriage",obj2.getJSONObject("seat").getString("seatNo"));
                                transdata.add(map1);
                            }
                            map.put("passenger",transdata);
                            dataList.add(map);
                        }
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
    }
}
