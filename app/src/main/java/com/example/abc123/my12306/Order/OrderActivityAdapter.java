package com.example.abc123.my12306.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;

public class OrderActivityAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;
    private boolean b;

    public OrderActivityAdapter(Context context, List<Map<String, Object>> data,boolean b) {
        this.context = context;
        this.data = data;
        this.b=b;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_order_item,null);
            viewHolder.tv1 = convertView.findViewById(R.id.tv_name);
            viewHolder.tv2 = convertView.findViewById(R.id.tv_train);
            viewHolder.tv3 = convertView.findViewById(R.id.tv_time);
            viewHolder.tv4 = convertView.findViewById(R.id.tv_carriage);
            viewHolder.imageView=convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText((String) data.get(position).get("name"));
        viewHolder.tv2.setText((String) data.get(position).get("train"));
        viewHolder.tv3.setText((String)data.get(position).get("time"));
        viewHolder.tv4.setText((String)data.get(position).get("carriage"));
        viewHolder.imageView.setImageResource(R.drawable.forward_25);
        if (!b){
            viewHolder.imageView.setVisibility(View.GONE);
        }
        return convertView;
    }
    public class ViewHolder {
        private TextView tv1,tv2,tv3,tv4;
        private ImageView imageView;
    }
}
