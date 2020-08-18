package com.example.abc123.my12306.Ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.Ticketadapter;

public class Tickettwo extends AppCompatActivity {
    private ListView listView;
    private List<Map<String,Object>> list;
    Calendar rightNow = Calendar.getInstance();
    private TextView preday,afterday,texttime;
    private int cyear = rightNow.get(Calendar.YEAR);
    private int cmonth = rightNow.get(Calendar.MONTH);
    private int cdate = rightNow.get(Calendar.DATE);
    private int cweek = rightNow.get(Calendar.DAY_OF_WEEK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickettwo);
        listView = findViewById(R.id.lv_ticketBuy);
        preday = findViewById(R.id.preday);
        afterday = findViewById(R.id.afterday);
        texttime = findViewById(R.id.texttime);
        texttime.setText(cyear+"-"+(cmonth+1)+"-"+cdate);
        list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("seattype","无座");
        map1.put("ticket","39张");
        map1.put("price","￥154.0");
        list.add(map1);
        Ticketadapter ticketadapter = new Ticketadapter(this,list);
        listView.setAdapter(ticketadapter);

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

                DatePickerDialog date = new DatePickerDialog(Tickettwo.this,listener,cyear,cmonth,cdate){
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
