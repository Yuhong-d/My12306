package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
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
import android.text.format.DateUtils;
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
    Calendar calendar = Calendar.getInstance();

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
            map1.put("seattype",map.get("seat"+i).toString().split(":")[0]);
            map1.put("ticket",map.get("seat"+i).toString().split(":")[1]);
            map1.put("price",map.get("price"+i).toString());
            data.add(map1);
        }
        map.put("fromStationName",tvTo.getText().toString().split("-->")[0]);
        map.put("toStationName",tvTo.getText().toString().split("-->")[1]);
        map.put("startTrainDate",texttime.getText().toString());
        ticketadapter = new Ticketadapter(this,data,map);
        listView.setAdapter(ticketadapter);
        tv1=findViewById(R.id.tvTrainNo);tv1.setText(map.get("banci").toString());
        tv2=findViewById(R.id.tvStarTime);tv2.setText(map.get("tv_start").toString());
        tv3=findViewById(R.id.tvArriveTime);tv3.setText(map.get("tv_end").toString());
        tv4=findViewById(R.id.tvDurationTime);tv4.setText("历时"+map.get("durationTime").toString());

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
                        Toast.makeText(Tickettwo.this,"网络错误！",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Tickettwo.this,"查询不到当日车票！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Tickettwo.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Tickettwo.this,"查询不到当日车票！",Toast.LENGTH_SHORT).show();
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
                data.clear();
                ticketadapter.notifyDataSetChanged();
                String oldDateFrom = texttime.getText().toString();
                Log.d("date", oldDateFrom);
                int oldYear = Integer.parseInt(oldDateFrom.split("-")[0]);
                final int oldMonth = Integer.parseInt(oldDateFrom.split("-")[1])-1 ;
                final int oldDay = Integer.parseInt(oldDateFrom.split("-")[2].split(" ")[0]);
                calendar.set(oldYear, oldMonth, oldDay);
                final Calendar calendarNew = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                String weekDay = DateUtils.formatDateTime(Tickettwo.this, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_WEEKDAY);
                texttime.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + weekDay);

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
                        Message msg=handlerPre.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("fromStationName",tvTo.getText().toString().split("->")[0])
                                .add("toStationName",tvTo.getText().toString().split("->")[1])
                                .add("startTrainDate",texttime.getText().toString().split(" ")[0])
                                .add("trainNo",tv1.getText().toString())
                                .build();

                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Train")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            String responsedata=response.body().string();
                            if (response.isSuccessful()&&!responsedata.equals("null")){
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
                                msg.what=3;
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
                data.clear();
                ticketadapter.notifyDataSetChanged();
                String oldDateFrom = texttime.getText().toString();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                String weekDay = DateUtils.formatDateTime(Tickettwo.this, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_WEEKDAY);
                texttime.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + weekDay);

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
                        Message msg=handlerNext.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("fromStationName",tvTo.getText().toString().split("->")[0])
                                .add("toStationName",tvTo.getText().toString().split("->")[1])
                                .add("startTrainDate",texttime.getText().toString().split(" ")[0])
                                .add("trainNo",tv1.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Train")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            String responsedata=response.body().string();
                            if (response.isSuccessful()&&!responsedata.equals("null")){
                                Log.d("Tickettwo", "responsedata:"+responsedata);
//                                if (responsedata==null){
//                                    Toast.makeText(Tickettwo.this,"没有当前车次！",Toast.LENGTH_SHORT).show();
//                                    data.clear();
//                                }
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
                               // data.clear();
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
                                msg.what=3;
                                //Toast.makeText(Tickettwo.this,"没有当前车次！",Toast.LENGTH_SHORT).show();
                                Log.d("Tickettwo", "run: "+msg.what);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.what=2;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.what=2;
                        }
                        handlerNext.sendMessage(msg);
                    }
                }.start();
            }
        });
    }
}
