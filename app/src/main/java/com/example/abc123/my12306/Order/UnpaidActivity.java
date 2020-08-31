package com.example.abc123.my12306.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.PaidSuccessActivity;
import com.example.abc123.my12306.Ticket.TicketBookingActivity;
import com.example.abc123.my12306.Ticket.TicketSubmitActivity;
import com.example.abc123.my12306.util.NetUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UnpaidActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, Object>> dataList;
    private TextView tv,tvCancel,tvConfirm;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //跳转到成功支付界面
                    int result=msg.arg1;
                    if (result==0){
                       //失败
                        Toast.makeText(UnpaidActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                    }else if (result==1) {
                    Intent intent=new Intent(UnpaidActivity.this, OrderSuccessActivity.class);
                    intent.putExtra("num",tv.getText().toString());
                    intent.putExtra("data", (Serializable) dataList);
                    startActivity(intent);
                    UnpaidActivity.this.finish();
                    }else{
                        Toast.makeText(UnpaidActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(UnpaidActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private Handler Cancelhandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int result=msg.arg1;
                    if (result==0){
                        //失败
                        Toast.makeText(UnpaidActivity.this,"取消失败",Toast.LENGTH_SHORT).show();

                    }else if (result==1) {
                        Toast.makeText(UnpaidActivity.this,"您已取消订单",Toast.LENGTH_SHORT).show();
//                        finish();
                        Unpaid.instance.getActivity().finish();
                        Intent intent1=new Intent(UnpaidActivity.this,Unpaid.instance.getActivity().getClass());
                        intent1.putExtra("page",1);
                        startActivity(intent1);
                        finish();

                    }else{
                        Toast.makeText(UnpaidActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(UnpaidActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        ls=findViewById(R.id.ls);
        tv=findViewById(R.id.tv_ordernum);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);
        Intent intent=getIntent();
        tv.setText(intent.getStringExtra("num"));//orderId
        //传递的数据，可用
        dataList= (List<Map<String, Object>>) intent.getSerializableExtra("data");
//        Map<String,String> map=new HashMap<>();
//        map.put("name","冬不拉");
//        map.put("train",intent.getStringExtra("train"));
//        map.put("time",intent.getStringExtra("time"));
//        map.put("carriage","2车19号");
//        dataList.add(map);
        OrderActivityAdapter activityAdapter=new OrderActivityAdapter(UnpaidActivity.this,dataList,false);
        ls.setAdapter(activityAdapter);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetUtils.check(UnpaidActivity.this)) {
                    Toast.makeText(UnpaidActivity.this, "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message msg=Cancelhandler.obtainMessage();
                        OkHttpClient client = new OkHttpClient();
                        //获取sessionId
                        SharedPreferences sp=getSharedPreferences("userinfo", MODE_PRIVATE);
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("orderId",tv.getText().toString())//请求体
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Cancel")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();


                            if (response.isSuccessful()){
                                String responsedata=response.body().string();
                                Log.d("UnpaidActivity", "responsedata: "+responsedata);
                                msg.arg1=Integer.parseInt(responsedata.substring(1,2));
                                msg.what=1;
                            }else {
                                msg.what=2;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.what=2;
                        }
                       Cancelhandler.sendMessage(msg);
                    }
                }.start();
            }

        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(UnpaidActivity.this, PaidSuccessActivity.class);
//                intent.putExtra("name","孔乙己");
//                intent.putExtra("train","G109");
//                intent.putExtra("time","2016-4-6");
//                intent.putExtra("carriage","2车10号");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);


                if (!NetUtils.check(UnpaidActivity.this)) {
                    Toast.makeText(UnpaidActivity.this, "网络异常，请检查！",
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
                        SharedPreferences sp=getSharedPreferences("userinfo", MODE_PRIVATE);
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("orderId",tv.getText().toString())//请求体
                                .build();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/Pay")
                                .addHeader("cookie", sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();


                            if (response.isSuccessful()){
                                String responsedata=response.body().string();
                                Log.d("UnpaidActivity", "responsedata: "+responsedata);
                                msg.arg1=Integer.parseInt(responsedata.substring(1,2));
                                msg.what=1;
                            }else {
                                msg.what=2;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.what=2;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }

        });
    }
}
