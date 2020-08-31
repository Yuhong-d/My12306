package com.example.abc123.my12306.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.SearchListDbOperation;
import com.example.abc123.my12306.Ticket.CityActivity;
import com.example.abc123.my12306.Ticket.Ticketone;
import com.example.abc123.my12306.User.my_contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TicketFragment extends Fragment {
    private static String TAG="Query:";
    private static final String PRE_SEARCH_HISTORY = "pre_search_history";
    private static final String SEARCH_HISTORY = "search_history";
    private TextView tv_star,tv_end;
    private String stationFrom, stationTo;//动画部分
    private TextView tv_texttime;
    private TextView tv_time;
    private Button bt_search;
    private TextView history;
    private ListView listView;
    private ArrayAdapter<String> mArrAdapter;
    private List<String> mHistoryKeywords;
    private SharedPreferences sp;//记录时间，地点等
    private SharedPreferences.Editor editor;
    private ImageView turn;
    private ArrayAdapter<String> arrayAdapter;
    private Myadapter mAdapter;
    private List<String> mData;
    private TicketFragment mContext;
    private SharedPreferences sp2;
    private ArrayList<Map<String,String>> tansData;//ticketone的数据
    private ArrayList<Map<String,Object>> tansData2;//tickettwo的数据
    
    private List<String> searchRecordsList;//创建的类中所用的list
    private List<String> tempList;//临时列表
    private SearchListDbOperation searchListDbOperation;
    
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent=new Intent(getContext(),Ticketone.class);
                    String s1 = tv_star.getText().toString();
                    String s2 = tv_end.getText().toString();
                    intent.putExtra("start",s1);
                    intent.putExtra("end",s2);
                    intent.putExtra("startTicketDate",tv_time.getText().toString());
                    intent.putExtra("list",tansData);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public  TicketFragment(){
        //需要空的构造方法
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ticket_fragment,container,false);
    }
     @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_star=view.findViewById(R.id.tv_start);
        tv_end=view.findViewById(R.id.tv_end);
         tv_texttime=view.findViewById(R.id.texttime);
         tv_time=view.findViewById(R.id.time);
         Calendar rightNow = Calendar.getInstance();
         tv_time.setText(rightNow.get(Calendar.YEAR)+"-"+(rightNow.get(Calendar.MONTH)+1)+"-"+rightNow.get(Calendar.DATE));
         listView=view.findViewById(R.id.listView);
        bt_search=view.findViewById(R.id.bt_search);
        turn = view.findViewById(R.id.turn);
         ImageView turn = getActivity().findViewById(R.id.turn);
         turn.setImageResource(R.drawable.turn);
        
        //历史查询
         //SQL表操作
         searchListDbOperation=new SearchListDbOperation(getContext(),"building");
         searchRecordsList=new ArrayList<>();
         tempList=new ArrayList<>();
         tempList=searchListDbOperation.getRecordsList();
         reversedList();

        //城市导航
         tv_star.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), CityActivity.class);
                 startActivityForResult(intent,110);
             }
         });
         tv_end.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), CityActivity.class);
                 startActivityForResult(intent,120);
             }
         });

         turn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 /*String s1 = tv_star.getText().toString();
                 String s2 = tv_end.getText().toString();
                 tv_star.setText(s2);
                 tv_end.setText(s1);
                 Log.d("Ticket", "onClick: "+s1+s2);*/
                 stationFrom = tv_star.getText().toString();
                 stationTo = tv_end.getText().toString();
                 TranslateAnimation animationFrom = new TranslateAnimation(0, 650, 0, 0);
                 TranslateAnimation animationTo = new TranslateAnimation(0, -650, 0, 0);
                 animationFrom.setDuration(400);
                 animationTo.setDuration(400);
                 animationFrom.setInterpolator(new AccelerateInterpolator());
                 animationTo.setInterpolator(new AccelerateInterpolator());
                 animationFrom.setAnimationListener(new Animation.AnimationListener() {
                     @Override
                     public void onAnimationStart(Animation animation) {
                     }

                     @Override
                     public void onAnimationEnd(Animation animation) {
                         tv_star.setText(stationTo);
                         tv_end.setText(stationFrom);
                     }

                     @Override
                     public void onAnimationRepeat(Animation animation) {
                     }
                 });
                 tv_star.startAnimation(animationFrom);
                 tv_end.startAnimation(animationTo);
             }
         });
         final Calendar oldCalendar = Calendar.getInstance();
         final int oldYear = oldCalendar.get(Calendar.YEAR);
         final int oldMonth = oldCalendar.get(Calendar.MONTH);
         final int oldDay = oldCalendar.get(Calendar.DATE);
         String weekDay = DateUtils.formatDateTime(getActivity(),oldCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_WEEKDAY);
         tv_time.setText(oldYear+"-"+(oldMonth+1)+"-"+oldDay+" "+weekDay);



         tv_time.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View v) {
                 DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     Calendar newCalendar = Calendar.getInstance();
                     //这个设置就是改成选择的日期，覆盖
                     newCalendar.set(year,month,dayOfMonth);//里面这个day是你选择的,这样就能星期就在随你点的变了
                     //newCalendar.getTimeInMillis(),精确到了几号
                     //  String week = WeekUtils.getWeek(year + "-" + (month + 1) + "-" + dayOfMonth);
                     String weekDay = DateUtils.formatDateTime(getActivity(),newCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_WEEKDAY);
                     //明明选的10月份，实际你也要加一,有意思,且格式要对，像下面这样
                     tv_time.setText(year+"-"+(month+1)+"-"+dayOfMonth+" "+weekDay);//系统的月份是从0开始的所以加一
                 }
             },oldYear,oldMonth,oldDay);//这个是默认日历表选中的日期，虽是7但，代表的是8，1.所以月份取出来要加1.
             //有时又不加1
                 Log.d("month",""+oldMonth);
                 dpd.setTitle("日期");
             long now = System.currentTimeMillis() - 1000;
                 dpd.getDatePicker().setMaxDate(now+(1000*60*60*24*3));  //设置日期最大值,只能选当前日期后三天
                 dpd.getDatePicker().setMinDate(oldCalendar.getTimeInMillis()); //设置日期最小值,当前日期之前的不能选
                 dpd.show();
         }

         });
         //查询按钮点击事件
         bt_search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final String first=tv_star.getText().toString().trim();
                 final String last=tv_end.getText().toString().trim();
                 String record=first+"->"+last;
                //删除表
                //判断数据库中是否存在该记录
                if (!searchListDbOperation.isHasRecord(record)) {
                    tempList.add(record);
                }else{
                    tempList.remove(searchListDbOperation.isHasRecord(record));
                    tempList.add(record);
                }
                //将搜索记录保存至数据库中
                searchListDbOperation.addRecords(record);
                reversedList();
                arrayAdapter.notifyDataSetChanged();
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
                         sp2=getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                         String sessionId =sp2.getString("cookie","");
                         RequestBody requestBody=new FormBody.Builder()
                                 .add("fromStationName",tv_star.getText().toString())
                                 .add("toStationName",tv_end.getText().toString())
                                 .add("startTrainDate",tv_time.getText().toString().split(" ")[0])
                                 .build();
                         Log.d(TAG, "requestbody: "+tv_time.getText().toString().split(" ")[0]);
                         Request request = new Request.Builder()
                                 .url("http://10.0.2.2:8080/My12306/otn/TrainList")
                                 .addHeader("cookie", sessionId)
                                 .post(requestBody)
                                 .build();
                         try {
                             Response response=client.newCall(request).execute();
                             Log.d(TAG, "run: "+response);

                             if (response.isSuccessful()){
                                 String responsedata=response.body().string();
                                 Log.d(TAG, "responsedata: "+responsedata);
                                 Log.d(TAG, "run: "+response.code());
                                 //解析数据，json
                                 tansData=new ArrayList<>();//查询结果
                                 tansData2=new ArrayList<>();//列车详情
                                 JSONArray jsonArray1=new JSONArray(responsedata);

                                 for (int i=0;i<jsonArray1.length();i++){
                                     Map<String,String>map=new HashMap<>();
                                     JSONObject obj1= (JSONObject) jsonArray1.get(i);
                                     map.put("banci",obj1.getString("trainNo"));
                                     map.put("tv_start",obj1.getString("startTime"));
                                     String dayDifference=obj1.getString("dayDifference");
                                     map.put("tv_end",obj1.getString("arriveTime")+"("+dayDifference+"日)");
                                     map.put("durationTime",obj1.getString("durationTime"));
                                     JSONObject obj2= (JSONObject) obj1.get("seats");
                                     //通过迭代器获取这段json当中所有的key值
                                     Iterator keys = obj2.keys();
                                     //然后通过一个循环取出所有的key值
                                     int m=1;
                                     String num=null;
                                     while (keys.hasNext()){
                                         String key = String.valueOf(keys.next());
                                         //最后就可以通过刚刚得到的key值去解析后面的json了
                                         JSONObject object1=obj2.getJSONObject(key);
                                         num=object1.getString("seatNum");
                                            switch (m){
                                            case 1:
                                                map.put("seat1",object1.getString("seatName")+":"+num);
                                                map.put("price1",object1.getString("seatPrice"));
                                                break;
                                            case 2:
                                                map.put("seat2",object1.getString("seatName")+":"+num);
                                                map.put("price2",object1.getString("seatPrice"));
                                                break;
                                            case 3:
                                                map.put("seat3",object1.getString("seatName")+":"+num);
                                                map.put("price3",object1.getString("seatPrice"));
                                                break;
                                            case 4:
                                                map.put("seat4",object1.getString("seatName")+":"+num);
                                                map.put("price4",object1.getString("seatPrice"));
                                                break;
                                         }
                                         m++;
                                         Log.d(TAG, "run: "+m);
                                     }
                                     if (m == 2){
                                         map.put("seat3","");
                                         map.put("seat4","");
                                     }
                                     if (m==3){
                                         map.put("seat4","");
                                     }
                                     tansData.add(map);
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
         });
     }
     //座位
    private void setSeat1(JSONObject obj2,Map<String,String>map) throws JSONException {
        if (obj2.get("高级软卧")!=null){
            JSONObject object1=obj2.getJSONObject("高级软卧");
            String num=object1.getString("seatNum");
            map.put("seat1",object1.getString("seatName")+":"+num);
        }else if(obj2.get("商务座")!=null){
            JSONObject object1=obj2.getJSONObject("商务座");
            String num=object1.getString("seatNum");
            map.put("seat1",object1.getString("seatName")+":"+num);
        }else if (obj2.get("硬座")!=null){
            JSONObject object1=obj2.getJSONObject("硬座");
            String num=object1.getString("seatNum");
            map.put("seat1",object1.getString("seatName")+":"+num);
        }else if(obj2.get("软座")!=null){
            JSONObject object1=obj2.getJSONObject("软座");
            String num=object1.getString("seatNum");
            map.put("seat1",object1.getString("seatName")+":"+num);
        }
    }
     //颠倒list顺序，用户输入的信息会从上依次往下显示
    private void reversedList() {
        searchRecordsList.clear();
        for(int i = tempList.size() - 1 ; i >= 0 ; i --){
            searchRecordsList.add(tempList.get(i));
        }
        String[] seplace={searchRecordsList.get(0),searchRecordsList.get(1)};
        arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.account_list_itemhis,R.id.historytv,searchRecordsList);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"resultcode"+resultCode);
        if (resultCode==404){
            Log.d(TAG, "onActivityResult: ");
            Toast.makeText(getContext(),"未选择城市",Toast.LENGTH_SHORT);
            return;
        }
        String s=data.getStringExtra("city");
        switch (requestCode){
            case 110:
                tv_star.setText(s);
                break;
            case 120:
               tv_end.setText(s);
               break;
        }
    }
}
