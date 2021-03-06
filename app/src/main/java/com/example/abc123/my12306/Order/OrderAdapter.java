package com.example.abc123.my12306.Order;

import android.content.Context;
import android.content.Intent;
import android.media.TimedMetaData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data,UnpaidData,PaidData;


    public OrderAdapter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.order_layout_item,null);
            viewHolder.tv1 = convertView.findViewById(R.id.tv_odr_num);
            viewHolder.tv2 = convertView.findViewById(R.id.tv_paystate);
            viewHolder.tv3 = convertView.findViewById(R.id.tv_train);
            viewHolder.tv4 = convertView.findViewById(R.id.tv_time);
            viewHolder.tv5=convertView.findViewById(R.id.tv_sta_end);
            viewHolder.tv6=convertView.findViewById(R.id.tv_person);
            viewHolder.tv7=convertView.findViewById(R.id.tv_price);
            viewHolder.imageView=convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText((String) data.get(position).get("num"));
        viewHolder.tv2.setText((String) data.get(position).get("type"));
        viewHolder.tv3.setText((String)data.get(position).get("train"));
        viewHolder.tv4.setText((String)data.get(position).get("time"));
        viewHolder.tv5.setText((String)data.get(position).get("end"));
        viewHolder.tv6.setText((String)data.get(position).get("person"));
        viewHolder.tv7.setText((String)data.get(position).get("price"));
        switch ((String) data.get(position).get("type")){
            case "已支付":
                viewHolder.tv2.setTextColor(context.getResources().getColor(R.color.blue));
                break;
            case "未支付":
                viewHolder.tv2.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case "已取消":
                viewHolder.tv2.setTextColor(context.getResources().getColor(R.color.darkgray));
                break;
        }
        viewHolder.imageView.setImageResource(R.drawable.forward_25);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("num",data.get(position).get("num").toString());
                switch ( data.get(position).get("type").toString()){
                    case "已支付":
                        intent.setClass(context,PaidActivity.class);
                        intent.putExtra("data", (Serializable) data.get(position).get("passenger"));
                        context.startActivity(intent);
                        break;
                    case "未支付":
                        intent.setClass(context,UnpaidActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("data", (Serializable) data.get(position).get("passenger"));
                        context.startActivity(intent);
                        break;
                }
            }
        });
        return convertView;
    }
    public class ViewHolder {
        private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        private ImageView imageView;
    }
}
