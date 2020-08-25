package com.example.abc123.my12306.Ticket.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.AddUserInfo;
import com.example.abc123.my12306.Ticket.TicketBookingActivity;
import com.example.abc123.my12306.Ticket.Ticketone;
import com.example.abc123.my12306.Ticket.Tickettwo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Ticketadapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;
    Map<String,Object> map;

    public Ticketadapter(Context context, List<Map<String, Object>> data,Map<String,Object> map) {
        this.context = context;
        this.data = data;
        this.map=map;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.tickettwo_item,null);
            viewHolder.tv_seattype = convertView.findViewById(R.id.tv_seattype);
            viewHolder.tv_ticket = convertView.findViewById(R.id.tv_ticket);
            viewHolder.tv_price = convertView.findViewById(R.id.tv_price);
            viewHolder.btn = convertView.findViewById(R.id.btn_buy);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_seattype.setText((String) data.get(position).get("seattype"));
        viewHolder.tv_ticket.setText((String) data.get(position).get("ticket"));
        viewHolder.tv_price.setText((String)data.get(position).get("price"));
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, TicketBookingActivity.class);
                intent.putExtra("dataMap", (Serializable) map);
                intent.putExtra("seat",data.get(position).get("seattype").toString());
                intent.putExtra("ticketNum",data.get(position).get("ticket").toString());
                intent.putExtra("price",data.get(position).get("price").toString());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_seattype;
        private TextView tv_ticket;
        private TextView tv_price;
        private Button btn;
    }
}