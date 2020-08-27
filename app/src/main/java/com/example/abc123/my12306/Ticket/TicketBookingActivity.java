package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.Order.PaidActivity;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.MyAdapter;
import com.example.abc123.my12306.User.my_contact;
import com.example.abc123.my12306.User.my_detailcontact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TicketBookingActivity extends AppCompatActivity {
    private static String TAG="TicketBooking";
    private TextView tv_addhuman,tv_submit;//添加乘客
    private ListView listView;
    private List<Map<String,Object>> data;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tvPrice,tvSeat,tvTotal;
    private MyAdapter adapter;
    private ArrayList<Map<String,Object>>transdata;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "msg的数据： "+msg);
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    Toast.makeText(TicketBookingActivity.this,"提交成功！",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(TicketBookingActivity.this, TicketSubmitActivity.class);
                    intent.putExtra("data",transdata);
                    intent.putExtra("orderId",msg.obj.toString());
                    startActivity(intent);
                    TicketBookingActivity.this.finish();
                    break;
                case 2:
                    Toast.makeText(TicketBookingActivity.this,"数据错误！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ticket_booking);

        tv1=findViewById(R.id.starpl);
        tv2=findViewById(R.id.endpl);
        tv3=findViewById(R.id.ticknum);
        tv4=findViewById(R.id.ticktime);
        tv5=findViewById(R.id.startime);
        tv6=findViewById(R.id.endtime);
        tvSeat=findViewById(R.id.tickleft);
        tvPrice=findViewById(R.id.tvPrice);//单价
        tvTotal=findViewById(R.id.tvTotal);//订单总额
        Intent intent=getIntent();
        final Map<String,Object> map= (Map<String, Object>) intent.getSerializableExtra("dataMap");
        tv1.setText(map.get("fromStationName").toString());
        tv2.setText(map.get("toStationName").toString());
        tv3.setText(map.get("banci").toString());
        tv4.setText(map.get("startTrainDate").toString()+"("+map.get("tv_end").toString().split("\\(")[1]);
        tv5.setText(map.get("tv_start").toString());
        tv6.setText(map.get("tv_end").toString().split("\\(")[0]);
        tvSeat.setText(intent.getStringExtra("seat")+"("+intent.getStringExtra("ticketNum")+"张)");
        tvPrice.setText(intent.getStringExtra("price"));
        listView=findViewById(R.id.ls);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int j, long id) {
                new androidx.appcompat.app.AlertDialog.Builder(TicketBookingActivity.this)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setTitle("提示")
                        .setMessage("确定删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                double price= Double.parseDouble(tvPrice.getText().toString());
                                data.remove(j);
                                adapter.notifyDataSetChanged();
                                tvTotal.setText("订单总额：￥"+price*data.size());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
                return false;
            }
        });
                tv_addhuman = findViewById(R.id.tv_addhuman);
                tv_addhuman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TicketBookingActivity.this, AddUserInfo.class);
                        startActivityForResult(intent, 110);
                    }
                });
                tv_submit = findViewById(R.id.tv_submit);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Intent intent = new Intent(TicketBookingActivity.this, TicketSubmitActivity.class);
                        //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //   startActivity(intent);
                        if (!NetUtils.check(TicketBookingActivity.this)){
                            Toast.makeText(TicketBookingActivity.this,"当前网络不可用",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Log.d("Handler", "run: ");
                                SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
                                Message msg=handler.obtainMessage();
                                OkHttpClient client = new OkHttpClient();
                                //获取sessionId
                                String sessionId =sp.getString("cookie","");
                                FormBody.Builder builder=new FormBody.Builder();
                                builder.add("trainNo",tv3.getText().toString());
                                Log.d(TAG, "No="+tv3.getText().toString());
                                builder.add("startTrainDate",tv4.getText().toString().split(" ")[0]);
                                Log.d(TAG, "date="+tv4.getText().toString().split(" ")[0]);
                                builder.add("seatName",tvSeat.getText().toString().split("\\(")[0]);
                                Log.d(TAG, "seat"+tvSeat.getText().toString().split("\\(")[0]);

                                for (int i=0;i<data.size();i++){
                                    builder.add("id",data.get(i).get("idcard").toString().split("：")[1]);
                                    Log.d(TAG, "id="+data.get(i).get("idcard").toString().split("：")[1]);
                                    builder.add("idType",data.get(i).get("idcard").toString().split("：")[0]);
                                }

                               RequestBody requestBody=builder.build();

                                Request request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/My12306/otn/Order")
                                        .addHeader("cookie", sessionId)
                                        .post(requestBody)
                                        .build();
                                try {
                                    Response response=client.newCall(request).execute();
                                    Log.d(TAG, "responsecode"+response.code());
                                    if (response.isSuccessful()){
                                        String responsedata=response.body().string();
                                        Log.d(TAG, "responsedata"+responsedata);
                                        //ArrayList< Map<String,Object>> dataList=new ArrayList<>();//查询结果
                                        transdata=new ArrayList<>();
                                        JSONObject obj = new JSONObject(responsedata);
                                        String orderId = obj.getString("id");
                                        JSONArray jsonArray = obj.getJSONArray("passengerList");
                                        JSONObject object1 = obj.getJSONObject("train");
                                        for (int i=0;i<jsonArray.length();i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            Map<String, Object> map1 = new HashMap<>();
                                            map1.put("name", object.getString("name"));
                                            map1.put("train", object1.getString("trainNo"));
                                            map1.put("time", object1.getString("startTrainDate"));
                                            map1.put("carriage", object.getJSONObject("seat").getString("seatNo"));
                                            transdata.add(map1);
                                            }
                                        msg.what=1;
                                        msg.obj=orderId;
                                    }else {
                                        msg.what=2;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    msg.what=2;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                handler.sendMessage(msg);
                            }
                        }.start();
                    }
                });
    }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent d) {
                super.onActivityResult(requestCode, resultCode, d);
                if (resultCode == 404) {
                    Toast.makeText(TicketBookingActivity.this, "未选择乘车人", Toast.LENGTH_SHORT);
                    return;
                }
                switch (requestCode) {
                    case 110:
                        double price= Double.parseDouble(tvPrice.getText().toString());
                        data = (List<Map<String, Object>>) d.getSerializableExtra("data");
                        adapter = new MyAdapter(TicketBookingActivity.this, data);
                        listView.setAdapter(adapter);
                        tvTotal.setText("订单总额：￥"+price*data.size());
                        break;
                }
            }

            private class MyAdapter extends BaseAdapter {
                private Context mcontext;
                private List<Map<String, Object>> datalist;


                public MyAdapter(Context context, List<Map<String, Object>> data) {
                    mcontext = context;
                    datalist = data;
                }

                @Override
                public int getCount() {
                    return datalist.size();
                }

                @Override
                public Object getItem(int position) {
                    return datalist.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHodler viewHodler = null;
                    if (convertView == null) {
                        viewHodler = new ViewHodler();
                        convertView = LayoutInflater.from(mcontext).inflate(R.layout.ticket_addusr_list_item, null);
                        viewHodler.tv1 = convertView.findViewById(R.id.tvName);
                        viewHodler.tv2 = convertView.findViewById(R.id.tvIdCard);
                        viewHodler.tv3 = convertView.findViewById(R.id.tvTel);
                        viewHodler.cbx_addusr = convertView.findViewById(R.id.cbx_addusr);
                        viewHodler.img = convertView.findViewById(R.id.img);
                        convertView.setTag(viewHodler);
                    } else {
                        viewHodler = (ViewHodler) convertView.getTag();
                    }
                    viewHodler.tv1.setText(datalist.get(position).get("name").toString());
                    viewHodler.tv2.setText(datalist.get(position).get("idcard").toString());
                    viewHodler.tv3.setText(datalist.get(position).get("num").toString());
                    viewHodler.cbx_addusr.setVisibility(View.GONE);
                    viewHodler.img.setImageResource(R.drawable.cancel_25);
                    viewHodler.tv1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TicketBookingActivity.this);
                            builder.setTitle("提醒！");
                            builder.setMessage("确定要删除该乘客吗？");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            return false;
                        }
                    });
                    return convertView;
                }

                public class ViewHodler {
                    private CheckBox cbx_addusr;
                    private TextView tv1, tv2, tv3;
                    private ImageView img;
                }
            }


}
