package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.Passenger;
import com.example.abc123.my12306.User.my_addcontact;
import com.example.abc123.my12306.User.my_contact;
import com.example.abc123.my12306.User.my_detailcontact;
import com.example.abc123.my12306.util.Constant;
import com.example.abc123.my12306.util.Order;

import com.example.abc123.my12306.util.Train;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TicketBookingActivity extends AppCompatActivity {

    private TextView tv_total_price;//总价的TextView
    private TextView tv_go_to_pay;//去支付的TextView
    private TextView tv_addhuman,tv_submit;//添加乘客
    private double totalPrice = 0.00;//总价钱
    private int totalCount = 0;//总票数
    private ListView listView;
    private TicketResult2Adapter ticketResult2Adapter;
    private List<Map<String,Object>> data;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tvPrice,tvSeat;
  //  private MyAdapter adapter;
    private Passenger[] passengers;

    private static final String TAG = "TicketBooking";
    /*
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "msg的数据： "+msg);
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String result = msg.obj.toString();
                    // Log.d(TAG, "result的数据： "+result);
                    //     String a = "1";
                    if ("1".equals(msg.obj)){
                        Toast.makeText(TicketBookingActivity.this,"提交成功！",Toast.LENGTH_SHORT).show();
                       // my_contact.instance.finish();
                        Intent intent=new Intent(TicketBookingActivity.this,my_contact.class);
                        startActivity(intent);
                        TicketBookingActivity.this.finish();

                    }else {
                        Toast.makeText(TicketBookingActivity.this,"提交失败！",Toast.LENGTH_SHORT).show();
                        TicketBookingActivity.this.finish();
                    }
                    break;
                case 2:
                    Toast.makeText(TicketBookingActivity.this,"数据错误！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

     */
    private ProgressDialog progressDialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //if(progressDialog != null){
           //     progressDialog.dismiss();
           // }
            switch (msg.what){
                case 1:
                    Order order = (Order) msg.obj;
                    Log.d("handleMessage: ", String.valueOf(order));
                    Intent intent = new Intent();
                    intent.setClass(TicketBookingActivity.this,TicketSubmitActivity.class);
                    intent.putExtra("order",order);
                    startActivity(intent);
                    break;
                case 2:
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
        tvPrice=findViewById(R.id.tvPrice);
        Intent intent=getIntent();
        Map<String,Object> map= (Map<String, Object>) intent.getSerializableExtra("dataMap");
        tv1.setText(map.get("fromStationName").toString());
        tv2.setText(map.get("toStationName").toString());
        tv3.setText(map.get("banci").toString());
        tv4.setText(map.get("startTrainDate").toString()+"("+map.get("tv_end").toString().split("\\(")[1]);
        tv5.setText(map.get("tv_start").toString());
        tv6.setText(map.get("tv_end").toString().split("\\(")[0]);
        tvSeat.setText(intent.getStringExtra("seat")+"("+intent.getStringExtra("ticketNum")+"张)");
        tvPrice.setText(intent.getStringExtra("price"));
        listView=findViewById(R.id.ls);


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data.remove(i);
                ticketResult2Adapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });
/*
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
                                data.remove(j);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
                return false;
            }
        });
 */
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
                        Log.d("zz","这里1");
                        if (!NetUtils.check(TicketBookingActivity.this)){
                            Toast.makeText(TicketBookingActivity.this,"当前网络不可用",Toast.LENGTH_SHORT).show();
                            return;
                        }
               //         progressDialog = ProgressDialog.show(TicketBookingActivity.this,
                //                null,
                //                "正在加载中...",
                //                false,true);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Message msg = handler.obtainMessage();
                                try {
                                    URL url = new URL("http://10.0.2.2:8080/My12306/otn/Order");
                                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                    httpURLConnection.setRequestMethod("POST");
                                    httpURLConnection.setConnectTimeout(Constant.REQUEST_TIMEOUT);
                                    httpURLConnection.setReadTimeout(Constant.SO_TIMEOUT);//读取超时 单位毫秒
                                    //发送POST方法必须设置容下两行
                                    httpURLConnection.setDoOutput(true);
                                    httpURLConnection.setDoInput(true);
                                    Log.d("xx","这里2");
                                    //不使用缓存
                                    httpURLConnection.setUseCaches(false);
                                    SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                                    String value = sp.getString("Cookie","");
                                    Log.d("xx","这里3");
                                    //设置请求属性
                                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                                    httpURLConnection.setRequestProperty("Cookie",value);

                                    PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                                    //发送请求数据
                                    Log.d("zzNo",tv3.getText().toString());
                                    Log.d("zzDate",tv4.getText().toString().split("\\(")[0]);
                                    Log.d("zzName",tvSeat.getText().toString().split("\\(")[0]);
                                    String params = "trainNo="+ tv3.getText().toString()+
                                            "&startTrainDate="+tv4.getText().toString().split("\\(")[0]+
                                            "&seatName="+ tvSeat.getText().toString().split("\\(")[0];
                                    for (int i=0 ; i<passengers.length ; i++){
                                        params += "&id="+ passengers[i].getId()+
                                                "&idType=" + passengers[i].getIdType();
                                    }
                                    Log.d("params",params);
                                    Log.d("xx","这里4");
                                    printWriter.write(params);
                                    printWriter.flush();
                                    printWriter.close();

                                    int resultCode = httpURLConnection.getResponseCode();
                                    if (resultCode == HttpURLConnection.HTTP_OK){
                                        InputStream in = httpURLConnection.getInputStream();
                                        StringBuffer sb = new StringBuffer();
                                        String readLine = new String();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                                        while ((readLine = reader.readLine()) != null){
                                            sb.append(readLine).append("\n");
                                        }
                                        String result = sb.toString();
                                        Log.d("result",result);
                                        Log.d("xx","这里5");

                                        //解析JSON
                                        Gson gson = new GsonBuilder().create();
                                        Order orders = gson.fromJson(result,Order.class);
                                        msg.what = 1;
                                        msg.obj = orders;
                                        Log.d("xx,这里6", String.valueOf(orders));
                                    }else {
                                        msg.what = 2;
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    msg.what = 2;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    msg.what = 2;
                                }
                                Log.d("xx","这里7");
                                handler.sendMessage(msg);
                            }
                        }.start();

                    }
                });


            }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent datalist) {
        super.onActivityResult(requestCode, resultCode, datalist);
        switch (resultCode){
            case RESULT_OK:
                data = new ArrayList<Map<String, Object>>();
                data = (List<Map<String,Object>>)datalist.getSerializableExtra("contactdata");

                passengers = new Passenger[data.size()];
                for (int i=0;i<data.size();i++){
                    passengers[i] = (Passenger) data.get(i).get("passenger");
                    Log.d("", "onActivityResult: passenger-------->"+passengers[i]);
                }
                Log.d("ss", String.valueOf(data));
                //TODO 计算价钱
                //int PriceSum = Integer.parseInt(getIntent().getStringExtra("SeatPrice"));
                //Log.d("ss2", String.valueOf(PriceSum));
                //tvTicketPassengerStep3OrderSum.setText("订单总额:￥"+(int)(data.size() * tvTicketPassengerStep3SeatPrice.getText().toString().split("￥")[0]) +"元");
//                adapter = new SimpleAdapter(this,
//                        data,
//                        R.layout.item_ticket_information3,
//                        new String[]{"name","idCard","tel"},
//                        new int[]{R.id.tvTicketPassengerName3,R.id.tvTicketPassengerIdCard3,R.id.tvTicketPassengerTel3});
                ticketResult2Adapter = new TicketResult2Adapter(this,data);
                listView.setAdapter(ticketResult2Adapter);
                break;
        }
    }
/*
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

 */


}
