package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.util.Seat;
import com.example.abc123.my12306.util.Train;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Ticketone extends AppCompatActivity {
    private static String TAG = "Ticketone:";
    private ListView listView;
    private SharedPreferences sp;
    private Train[] trains;
    private ProgressDialog progressDialog;
    private List<Map<String, Object>> data;
    private SimpleAdapter Adapter;
    private TextView preday, afterday, texttime, tvTo;
    private List<Map<String,String>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketone);
        preday = findViewById(R.id.preday);
        afterday = findViewById(R.id.afterday);
        texttime = findViewById(R.id.texttime);
        tvTo = findViewById(R.id.star_end);
        listView = findViewById(R.id.lv_ticket);

        Intent intent = getIntent();
        tvTo.setText(getIntent().getStringExtra("start") + "-->" + getIntent().getStringExtra("end"));
           list = (List<Map<String, String>>) intent.getSerializableExtra("list");
        texttime.setText(getIntent().getStringExtra("startTicketDate"));


        //  texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
        preday.setOnClickListener(new MyTicketListener());
        afterday.setOnClickListener(new MyTicketListener());

        data = new ArrayList<Map<String, Object>>();
        Adapter = new SimpleAdapter(
                this,
                data,
                R.layout.ticket_item,
                new String[]{"TicketNum", "img1", "img2", "fromTime", "toTime", "seat1", "seat2", "seat3", "seat4"},
                new int[]{R.id.tv_banci, R.id.image_shi, R.id.image_zhong, R.id.tv_start, R.id.tv_over, R.id.tv_seat1, R.id.tv_seat2, R.id.tv_seat3, R.id.tv_seat4}
        );
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(Ticketone.this, Tickettwo.class);
                intent.putExtra("start-end",tvTo.getText().toString());
                intent.putExtra("trainNo",position);
                intent.putExtra("time",texttime.getText().toString());
                intent.putExtra("list", (Serializable) list);
                startActivity(intent);
            }
        });
        //开启异步任务去获取数据
        new TicketStep1Task().execute();
    }

    private class MyTicketListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            String oldDateFrom = texttime.getText().toString();
            Log.d("date", oldDateFrom);
            int oldYear = Integer.parseInt(oldDateFrom.split("-")[0]);
            int oldMonth = Integer.parseInt(oldDateFrom.split("-")[1]) - 1;
            int oldDay = Integer.parseInt(oldDateFrom.split("-")[2].split(" ")[0]);
            calendar.set(oldYear, oldMonth, oldDay);
            switch (view.getId()) {
                case R.id.preday:
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    break;
                case R.id.afterday:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
            }
            //更新TextView
            String weekDay = DateUtils.formatDateTime(Ticketone.this, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_WEEKDAY);
            texttime.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + weekDay);
            Log.d("yy", "这里2");
            new TicketStep1Task().execute();
        }
    }

    class TicketStep1Task extends AsyncTask<String, String, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("yy", "这里3");
      //      progressDialog = ProgressDialog.show(Ticketone.this,
       //             null,
       //             "正在加载中....",
       //             false, true);
        }

        @Override
        protected Object doInBackground(String... strings) {
            String resultObject = "";
            OkHttpClient client = new OkHttpClient();
            //获取sessionId
            sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            String sessionId = sp.getString("cookie", "");
            @SuppressLint("WrongThread") RequestBody requestBody = new FormBody.Builder()
                    .add("fromStationName", getIntent().getStringExtra("start"))
                    .add("toStationName", getIntent().getStringExtra("end"))
                    .add("startTrainDate", texttime.getText().toString().split(" ")[0])
                    .build();
            //   Log.d(TAG, "requestbody: "+texttime.getText().toString().split(" ")[0]);
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/My12306/otn/TrainList")
                    .addHeader("cookie", sessionId)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "run: " + response);

                if (response.isSuccessful()) {
                    String responsedata = response.body().string();
                    Log.d(TAG, "responsedata: " + responsedata);
                    Log.d(TAG, "run: " + response.code());
                    //解析Json
                    Gson gson = new GsonBuilder().create();
                    Train[] trains = gson.fromJson(responsedata, Train[].class);
                    //System.out.print(trains.toString());
                    Log.d("trains", trains.toString());
                    return trains;
                } else {
                    resultObject = "2";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resultObject = "2";
            } catch (IOException e) {
                e.printStackTrace();
                resultObject = "2";
            }
            return resultObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            System.out.print(result);
            data.clear();
            if (result instanceof Train[]) {
                trains = (Train[]) result;
                if (trains.length == 0) {
                    Toast.makeText(Ticketone.this, "没有对应的车次", Toast.LENGTH_SHORT).show();
                } else {
                    for (Train train : trains) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("TicketNum", train.getTrainNo());
                        if (train.getStartStationName().equals(train.getFromStationName())) {
                            map.put("img1", R.drawable.flg_shi);
                        } else {
                            map.put("img1", R.drawable.flg_guo);
                        }
                        if (train.getEndStationName().equals(train.getToStationName())) {
                            map.put("img2", R.drawable.flg_zhong);
                        } else {
                            map.put("img2", R.drawable.flg_guo);
                        }
                        map.put("fromTime", train.getStartTime());
                        map.put("toTime", train.getArriveTime() + "(" + train.getDayDifference() + ")");
                        String[] seatKey = {"seat1", "seat2", "seat3", "seat4"};
                        int i = 0;
                        Map<String, Seat> seats = train.getSeats();
                        for (String key : seats.keySet()) {
                            Seat seat = seats.get(key);
                            map.put(seatKey[i++], seat.getSeatName() + ":" + seat.getSeatNum());
                        }
                        data.add(map);
                    }
                }
                Adapter.notifyDataSetChanged();
            } else if (result instanceof String) {

            } else {
            }
        }
    }
}