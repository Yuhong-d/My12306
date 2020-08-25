package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.MainActivity;
import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.adpter.Ticketadapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tickettwo extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,Object>> list;
    private TextView tvTo;
    private TextView preday,afterday,texttime;
    private TextView tv1,tv2,tv3,tv4;
    Calendar rightNow = Calendar.getInstance();
    private Ticketadapter ticketadapter;
    private int cyear = rightNow.get(Calendar.YEAR);
    private int cmonth = rightNow.get(Calendar.MONTH);
    private int cdate ;
    private int current=rightNow.get(Calendar.DATE);
    private int cweek = rightNow.get(Calendar.DAY_OF_WEEK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickettwo);
        preday = findViewById(R.id.preday);
        afterday = findViewById(R.id.afterday);
        texttime = findViewById(R.id.texttime);
        tvTo=findViewById(R.id.tvTo);
        Intent intent=getIntent();
        tvTo.setText(intent.getStringExtra("start-end"));
        texttime.setText(intent.getStringExtra("time"));
        listView = findViewById(R.id.lv_ticketBuy);
        list= (List<Map<String, Object>>) intent.getSerializableExtra("list");
        int key=intent.getIntExtra("trainNo",1);
        final ArrayList<Map<String,Object>> data = new ArrayList<>();
        final Map<String,Object> map=list.get(key);
        int len= (int) (map.size()*0.5-1);
        for (int i=1;i<len;i++){
            Map<String,Object> map1 = new HashMap<>();
            Log.d("TwoTicket", "msg:"+"seat"+i+"len:"+len);
            map1.put("seattype",map.get("seat"+i).toString().split(":")[0]);
            map1.put("ticket",map.get("seat"+i).toString().split(":")[1]);
            map1.put("price",map.get("price"+i).toString());
            Log.d("TwoTicket", "price:"+map.get("price"+i).toString());
            data.add(map1);
        }

        ticketadapter = new Ticketadapter(this,data);
        listView.setAdapter(ticketadapter);
        tv1=findViewById(R.id.tvTrainNo);tv1.setText(map.get("banci").toString());
        tv2=findViewById(R.id.tvStarTime);tv2.setText(map.get("tv_start").toString());
        tv3=findViewById(R.id.tvArriveTime);tv3.setText(map.get("tv_end").toString());
        tv4=findViewById(R.id.tvDurationTime);tv4.setText("历时"+map.get("durationTime").toString());
        cdate=(Integer)Integer.parseInt(texttime.getText().toString().split("-")[2]) ;
        final Handler handlerPre=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Map<String,Object>map= (Map<String, Object>) msg.obj;
                        Log.d("TicketTwo", "handleMessage: "+data.toString());
                        ticketadapter.notifyDataSetChanged();
                        tv2.setText(map.get("tv_start").toString());
                        tv3.setText(map.get("tv_end").toString());
                        tv4.setText("历时"+map.get("durationTime").toString());
                        break;
                    case 2:
                        Toast.makeText(Tickettwo.this,"网络错误！",Toast.LENGTH_SHORT);
                        break;
                }
            }
        };
        final Handler handlerNext=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        Map<String,Object>map= (Map<String, Object>) msg.obj;
                        Log.d("TicketTwo", "handleMessage: "+data.toString());
                        ticketadapter.notifyDataSetChanged();
                        tv2.setText(map.get("tv_start").toString());
                        tv3.setText(map.get("tv_end").toString());
                        tv4.setText("历时"+map.get("durationTime").toString());
                        break;
                    case 2:
                        Toast.makeText(Tickettwo.this,"网络错误！",Toast.LENGTH_SHORT);
                        break;
                }
            }
        };
        final Handler handlerDate=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        Map<String,Object>map= (Map<String, Object>) msg.obj;
                        Log.d("TicketTwo", "handleMessage: "+data.toString());
                        ticketadapter.notifyDataSetChanged();
                        tv2.setText(map.get("tv_start").toString());
                        tv3.setText(map.get("tv_end").toString());
                        tv4.setText("历时"+map.get("durationTime").toString());
                        break;
                    case 2:
                        Toast.makeText(Tickettwo.this,"网络错误！",Toast.LENGTH_SHORT);
                        break;
                }
            }
        };

        //给此处的listview设置item点击事件不会起作用，原因是控件中包含了button，获取焦点，导致item无法获取焦点或焦点失效
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent();
//                intent.setClass(Tickettwo.this,TicketBookingActivity.class);
//                startActivity(intent);
//            }
//        });
        //前一天
        preday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdate>current){
                    cdate = cdate-1;
                    texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
                }else{
                    return;
                }
                if(!NetUtils.check(Tickettwo.this)){
                    Toast.makeText(Tickettwo.this, "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.d("Handler", "run: ");
                        SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
                        Message msg=handlerPre.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("fromStationName",tvTo.getText().toString().split("->")[0])
                                .add("toStationName",tvTo.getText().toString().split("->")[1])
                                .add("startTrainDate",texttime.getText().toString())
                                .add("trainNo",tv1.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Train")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            if (response.isSuccessful()){
                                String responsedata=response.body().string();
                                if (responsedata.equals("null")){
                                    msg.what=2;
                                    return;
                                }
                                Map<String,Object>map=new HashMap<>();
                                //数据解析，json
                                JSONObject obj=new JSONObject(responsedata);
                                map.put("tv_start",obj.getString("startTime"));
                                String datDiff=obj.getString("dayDifference");
                                map.put("tv_end",obj.getString("arriveTime")+"("+datDiff+"日)");
                                map.put("durationTime",obj.getString("durationTime"));
                                JSONObject object=obj.getJSONObject("seats");
                                Iterator keys = object.keys();
                                //然后通过一个循环取出所有的key值
                                int m=1;
                                String num=null;
                                data.clear();
                                while (keys.hasNext()){
                                    String key = String.valueOf(keys.next());
                                    //最后就可以通过刚刚得到的key值去解析后面的json了
                                    Map<String,Object> map1=new HashMap<>();
                                    JSONObject object1=object.getJSONObject(key);
                                    map1.put("seattype",key);
                                    map1.put("ticket",object1.getString("seatNum"));
                                    map1.put("price",object1.getString("seatPrice"));
                                    data.add(map1);
                                }
                                msg.what=1;
                                msg.obj=map;
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
                        handlerPre.sendMessage(msg);
                    }
                }.start();
            }
        });
        //后一天
        afterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdate<current+3){
                    cdate++;
                    texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
                }
                if(!NetUtils.check(Tickettwo.this)){
                    Toast.makeText(Tickettwo.this, "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        Log.d("TicketTwo", "Threadnext: ");
                        super.run();
                        SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
                        Message msg=handlerNext.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("fromStationName",tvTo.getText().toString().split("->")[0])
                                .add("toStationName",tvTo.getText().toString().split("->")[1])
                                .add("startTrainDate",texttime.getText().toString())
                                .add("trainNo",tv1.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Train")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            if (response.isSuccessful()){
                                String responsedata=response.body().string();
                                Map<String,Object>map=new HashMap<>();
                                if (responsedata.equals("null")){
                                    msg.what=2;
                                    return;
                                }
                                //数据解析，json
                                JSONObject obj=new JSONObject(responsedata);
                                map.put("tv_start",obj.getString("startTime"));
                                String datDiff=obj.getString("dayDifference");
                                map.put("tv_end",obj.getString("arriveTime")+"("+datDiff+"日)");
                                map.put("durationTime",obj.getString("durationTime"));
                                JSONObject object=obj.getJSONObject("seats");
                                Iterator keys = object.keys();
                                //然后通过一个循环取出所有的key值
                                int m=1;
                                String num=null;
                                data.clear();
                                while (keys.hasNext()){
                                    String key = String.valueOf(keys.next());
                                    //最后就可以通过刚刚得到的key值去解析后面的json了
                                    Map<String,Object> map1=new HashMap<>();
                                    JSONObject object1=object.getJSONObject(key);
                                    map1.put("seattype",key);
                                    map1.put("ticket",object1.getString("seatNum"));
                                    map1.put("price",object1.getString("seatPrice"));
                                    data.add(map1);
                                }
                                msg.what=1;
                                msg.obj=map;
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
                        handlerPre.sendMessage(msg);
                    }
                }.start();
            }
        });
        texttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cyear = year;
                        cmonth = month;
                        cdate = dayOfMonth;
                        String desc = String.format("%d-%d-%d",cyear,(cmonth+1),cdate);
                        texttime.setText(desc);
                        if(!NetUtils.check(Tickettwo.this)){
                            Toast.makeText(Tickettwo.this, "网络异常，请检查！",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
                                Message msg=handlerDate.obtainMessage();
                                OkHttpClient client = new OkHttpClient();
                                //获取sessionId
                                String sessionId =sp.getString("cookie","");
                                RequestBody requestBody=new FormBody.Builder()
                                        .add("fromStationName",tvTo.getText().toString().split("->")[0])
                                        .add("toStationName",tvTo.getText().toString().split("->")[1])
                                        .add("startTrainDate",texttime.getText().toString())
                                        .add("trainNo",tv1.getText().toString())
                                        .build();
                                Request request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/My12306/otn/Train")
                                        .addHeader("cookie", sessionId)
                                        .post(requestBody)
                                        .build();
                                try {
                                    Response response=client.newCall(request).execute();
                                    if (response.isSuccessful()){
                                        String responsedata=response.body().string();
                                        if (responsedata.equals("null")){
                                            msg.what=2;
                                            return;
                                        }
                                        Map<String,Object>map=new HashMap<>();
                                        //数据解析，json
                                        JSONObject obj=new JSONObject(responsedata);
                                        map.put("tv_start",obj.getString("startTime"));
                                        String datDiff=obj.getString("dayDifference");
                                        map.put("tv_end",obj.getString("arriveTime")+"("+datDiff+"日)");
                                        map.put("durationTime",obj.getString("durationTime"));
                                        JSONObject object=obj.getJSONObject("seats");
                                        Iterator keys = object.keys();
                                        //然后通过一个循环取出所有的key值
                                        int m=1;
                                        String num=null;
                                        data.clear();
                                        while (keys.hasNext()){
                                            String key = String.valueOf(keys.next());
                                            //最后就可以通过刚刚得到的key值去解析后面的json了
                                            Map<String,Object> map1=new HashMap<>();
                                            JSONObject object1=object.getJSONObject(key);
                                            map1.put("seattype",key);
                                            map1.put("ticket",object1.getString("seatNum"));
                                            map1.put("price",object1.getString("seatPrice"));
                                            data.add(map1);
                                        }
                                        msg.what=1;
                                        msg.obj=map;
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
                                handlerPre.sendMessage(msg);
                            }
                        }.start();
                    }
                };

                DatePickerDialog date = new DatePickerDialog(Tickettwo.this,listener,cyear,cmonth,cdate){
                    @Override
                    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newCalendar = Calendar.getInstance();
                        //这个设置就是改成选择的日期，覆盖
                        newCalendar.set(year,month,dayOfMonth);//里面这个day是你选择的,这样就能星期就在随你点的变了
//                        if (year<cyear)
//                            view.updateDate(cyear,cmonth,cdate);
//
//                        if (month < cmonth && year == cyear)
//                            view.updateDate(cyear,cmonth,cdate);
//
//                        if (dayOfMonth < cdate && year == cyear && month == cmonth)
//                            view.updateDate(cyear,cmonth,cdate);
                    }
                };
                long now = System.currentTimeMillis() - 1000;
                date.getDatePicker().setMaxDate(now+(1000*60*60*24*3));  //设置日期最大值,只能选当前日期后三天
                date.getDatePicker().setMinDate(rightNow.getTimeInMillis()); //设置日期最小值,当前日期之前的不能选
                date.show();
            }
        });
    }
}
