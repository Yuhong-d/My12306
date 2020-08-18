package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.abc123.my12306.R;

public class TicketBookingActivity extends AppCompatActivity {

    private TextView tv_price;//单价的TextView
    private TextView tv_total_price;//总价的TextView
    private TextView tv_go_to_pay;//去支付的TextView
    private TextView tv_addhuman,tv_submit;//添加乘客
    private double totalPrice = 0.00;//总价钱
    private int totalCount = 0;//总票数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ticket_booking);
        tv_addhuman=findViewById(R.id.tv_addhuman);
        tv_addhuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TicketBookingActivity.this,AddUserInfo.class);
                startActivity(intent);
            }
        });
        tv_submit=findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TicketBookingActivity.this,TicketSubmitActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
