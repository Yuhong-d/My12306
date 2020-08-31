package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.Order.OrderActivityAdapter;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.util.NetUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSubmitActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, Object>> dataList;
    private TextView tvCancel,tvConfirm,tvOrderId;
    private Button button;
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
                        Toast.makeText(TicketSubmitActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                    }else if (result==1) {
                        Intent intent=new Intent(TicketSubmitActivity.this,PaidSuccessActivity.class);
                        intent.putExtra("num",tvOrderId.getText().toString());
                        intent.putExtra("data", (Serializable) dataList);
                        startActivity(intent);
                        TicketSubmitActivity.this.finish();
                    }else{
                        Toast.makeText(TicketSubmitActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(TicketSubmitActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_submit);
        ls=findViewById(R.id.ls);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);
        tvOrderId=findViewById(R.id.tv_ordernum);
        Intent intent=getIntent();
        dataList= (List<Map<String, Object>>) intent.getSerializableExtra("data");
        tvOrderId.setText(intent.getStringExtra("orderId"));
        OrderActivityAdapter activityAdapter=new OrderActivityAdapter(TicketSubmitActivity.this,dataList,true);
        ls.setAdapter(activityAdapter);

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
//                Intent intent=new Intent(TicketSubmitActivity.this,PaidSuccessActivity.class);
//                intent.putExtra("name","孔乙己");
//                intent.putExtra("train","G109");
//                intent.putExtra("time","2016-4-6");
//                intent.putExtra("carriage","2车10号");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                if (!NetUtils.check(TicketSubmitActivity.this)) {
                    Toast.makeText(TicketSubmitActivity.this, "网络异常，请检查！",
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
                                .add("orderId",tvOrderId.getText().toString())//请求体
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
