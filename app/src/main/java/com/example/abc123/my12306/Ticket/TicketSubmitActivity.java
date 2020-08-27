package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.Order.OrderActivityAdapter;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.util.Constant;
import com.example.abc123.my12306.util.NetUtils;
import com.example.abc123.my12306.util.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class TicketSubmitActivity extends AppCompatActivity {
    private ListView ls;
    private TextView tv;
    private List<Map<String, String>> dataList;
    private TextView tvCancel,tvConfirm;
    private SimpleAdapter simpleAdapter;
    private Button button;
    private ProgressDialog progressDialog;
    private Order order;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog != null){
                progressDialog.dismiss();
            }
            switch (msg.what){
                case 1:
                    String result = msg.obj.toString();
                    if ("1".equals(result)){
                        Toast.makeText(TicketSubmitActivity.this,"支付成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(TicketSubmitActivity.this,PaidSuccessActivity.class);
                        intent.putExtra("order",order);
                        Log.d("order-----", String.valueOf(order));
                        startActivity(intent);
                    }else if ("0".equals(result)){
                        Toast.makeText(TicketSubmitActivity.this,"支付失败！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(TicketSubmitActivity.this,"服务器错误，请重试！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(TicketSubmitActivity.this,"请重新登录！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_submit);
        ls=findViewById(R.id.ls);
        tv=findViewById(R.id.tv_ordernum);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);

        order = (Order) getIntent().getSerializableExtra("order");
        tv.setText(order.getId());

        dataList=new ArrayList<Map<String, String>>();
        for (int i=0 ; i<order.getPassengerList().length ; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("ticketName", order.getPassengerList()[i].getName());
            map.put("ticketNo", order.getTrain().getTrainNo());
            map.put("ticketDate", order.getTrain().getStartTrainDate());
            map.put("ticketSeatName", "6车51号");
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(TicketSubmitActivity.this,
                dataList,
                R.layout.item_ticket_information4,
                new String[]{"ticket4Name","ticket4No","ticket4Date","ticket4SeatName"},
                new int[]{R.id.Ticket4Name,R.id.Ticket4No,R.id.Ticket4Date,R.id.Ticket4SeatName});
        ls.setAdapter(simpleAdapter);
       // OrderActivityAdapter activityAdapter=new OrderActivityAdapter(TicketSubmitActivity.this,dataList);
       // ls.setAdapter(activityAdapter);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TicketSubmitActivity.this,"请稍后在订单中查看",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zz","这里1");
                if (!NetUtils.check(TicketSubmitActivity.this)){
                    Toast.makeText(TicketSubmitActivity.this,"当前网络不可用",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(TicketSubmitActivity.this,
                        null,
                        "正在加载中....",
                        false,true);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message msg = handler.obtainMessage();
                        try {
                            URL url = new URL(Constant.Host+"/otn/Pay");
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
                            String params = "orderId="+ tv.getText().toString();
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
                                //Order orders = gson.fromJson(result,Order.class);
                                String orderString = gson.fromJson(result,String.class);
                                msg.what = 1;
                                msg.obj = orderString;
                                Log.d("xx,这里6", String.valueOf(orderString));
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
}
