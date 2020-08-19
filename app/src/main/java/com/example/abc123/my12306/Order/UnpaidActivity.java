package com.example.abc123.my12306.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.PaidSuccessActivity;
import com.example.abc123.my12306.Ticket.TicketBookingActivity;
import com.example.abc123.my12306.Ticket.TicketSubmitActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnpaidActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, String>> dataList;
    private TextView tv,tvCancel,tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        ls=findViewById(R.id.ls);
        tv=findViewById(R.id.tv_ordernum);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);
        Intent intent=getIntent();
        tv.setText(intent.getStringExtra("num"));
        dataList=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<>();
        map.put("name","冬不拉");
        map.put("train",intent.getStringExtra("train"));
        map.put("time",intent.getStringExtra("time"));
        map.put("carriage","2车19号");
        dataList.add(map);
        OrderActivityAdapter activityAdapter=new OrderActivityAdapter(UnpaidActivity.this,dataList);
        ls.setAdapter(activityAdapter);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UnpaidActivity.this,"您已取消订单",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UnpaidActivity.this, PaidSuccessActivity.class);
                intent.putExtra("name","孔乙己");
                intent.putExtra("train","G109");
                intent.putExtra("time","2016-4-6");
                intent.putExtra("carriage","2车10号");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
