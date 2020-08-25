package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.adpter.TickettwoAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ticketone extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,String>> list;
    private TextView preday,afterday,texttime,tvTo;
    Calendar rightNow = Calendar.getInstance();
    private int cyear = rightNow.get(Calendar.YEAR);
    private int cmonth = rightNow.get(Calendar.MONTH);
    private int cdate = rightNow.get(Calendar.DATE);
    private int cweek = rightNow.get(Calendar.DAY_OF_WEEK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketone);
        preday = findViewById(R.id.preday);
        afterday = findViewById(R.id.afterday);
        texttime = findViewById(R.id.texttime);
        tvTo=findViewById(R.id.star_end);
        Intent intent=getIntent();
        tvTo.setText(intent.getStringExtra("start")+"-->"+intent.getStringExtra("end"));
        list= (List<Map<String, String>>) intent.getSerializableExtra("list");
        texttime.setText(intent.getStringExtra("time"));
        listView = findViewById(R.id.lv_ticket);
        TickettwoAdapter tickettwoAdapter = new TickettwoAdapter(this,list);
        listView.setAdapter(tickettwoAdapter);
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
        preday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdate<30&&cdate>0){
                    cdate = cdate-1;
                    texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
                }
            }
        });
        afterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cdate<30&&cdate>0){
                    cdate++;
                    texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
                }
            }
        });
        texttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cyear = year;
                        cmonth = month;
                        cdate = dayOfMonth;
                        String desc = String.format("%d-%d-%d",cyear,(cmonth+1),cdate);
                        texttime.setText(desc);
                    }
                };

                DatePickerDialog date = new DatePickerDialog(Ticketone.this,listener,cyear,cmonth,cdate){
                    @Override
                    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
                        if (year<cyear)
                            view.updateDate(cyear,cmonth,cdate);

                        if (month < cmonth && year == cyear)
                            view.updateDate(cyear,cmonth,cdate);

                        if (dayOfMonth < cdate && year == cyear && month == cmonth)
                            view.updateDate(cyear,cmonth,cdate);
                    }
                };
                date.show();
            }
        });
    }
}
