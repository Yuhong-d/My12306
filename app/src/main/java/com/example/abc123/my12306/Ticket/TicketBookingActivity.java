package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketBookingActivity extends AppCompatActivity {

    private TextView tv_price;//单价的TextView
    private TextView tv_total_price;//总价的TextView
    private TextView tv_go_to_pay;//去支付的TextView
    private TextView tv_addhuman,tv_submit;//添加乘客
    private double totalPrice = 0.00;//总价钱
    private int totalCount = 0;//总票数
    private ListView listView;
    private List<Map<String,String>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ticket_booking);
        listView=findViewById(R.id.ls);
        Map<String,String>map=new HashMap<>();
        map.put("name","冬不拉");
        map.put("idcard",1111111111+"");
        map.put("number","1234343242");
        data=new ArrayList<>();
        data.add(map);
        MyAdapter adapter=new MyAdapter(TicketBookingActivity.this,data);
        listView.setAdapter(adapter);
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
    private class MyAdapter extends BaseAdapter {
        private Context mcontext;
        private List<Map<String,String>>datalist;


        public MyAdapter(Context context, List<Map<String, String>> data) {
            mcontext=context;
            datalist=data;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler=null;
            if(convertView == null){
                viewHodler=new ViewHodler();
                convertView= LayoutInflater.from(mcontext).inflate(R.layout.ticket_addusr_list_item,null);
                viewHodler.tv1=convertView.findViewById(R.id.tvName);
                viewHodler.tv2=convertView.findViewById(R.id.tvIdCard);
                viewHodler.tv3=convertView.findViewById(R.id.tvTel);
                viewHodler.cbx_addusr=convertView.findViewById(R.id.cbx_addusr);
                viewHodler.img=convertView.findViewById(R.id.img);
                convertView.setTag(viewHodler);
            }else {
                viewHodler= (ViewHodler) convertView.getTag();
            }
            viewHodler.tv1.setText(datalist.get(position).get("name"));
            viewHodler.tv2.setText(datalist.get(position).get("idcard"));
            viewHodler.tv3.setText(datalist.get(position).get("number"));
            viewHodler.cbx_addusr.setVisibility(View.GONE);
            viewHodler.img.setImageResource(R.drawable.cancel_25);
            return convertView;
        }
        public class ViewHodler{
            private CheckBox cbx_addusr;
            private TextView tv1,tv2,tv3;
            private ImageView img;
        }
    }
}
