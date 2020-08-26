package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.Order.OrderActivityAdapter;
import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSubmitActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, Object>> dataList;
    private TextView tvCancel,tvConfirm;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_submit);
        ls=findViewById(R.id.ls);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);

        dataList=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new HashMap<>();
        map.put("name","孔乙己");
        map.put("train","G109");
        map.put("time","2016-4-6");
        map.put("carriage","2车10号");
        dataList.add(map);
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
                Intent intent=new Intent(TicketSubmitActivity.this,PaidSuccessActivity.class);
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
