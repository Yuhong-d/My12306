package com.example.abc123.my12306.Ticket.adpter;

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

public class TickettwoAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> data;

    public TickettwoAdapter(Context context, List<Map<String, String>> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ticket_item,null);
            viewHolder.tv_banci = convertView.findViewById(R.id.tv_banci);
            viewHolder.tv_start = convertView.findViewById(R.id.tv_start);
            viewHolder.tv_over = convertView.findViewById(R.id.tv_over);
            viewHolder.tv_seat1 = convertView.findViewById(R.id.tv_seat1);
            viewHolder.tv_seat2 = convertView.findViewById(R.id.tv_seat2);
            viewHolder.tv_seat3 = convertView.findViewById(R.id.tv_seat3);
            viewHolder.tv_seat4 = convertView.findViewById(R.id.tv_seat4);
            viewHolder.img2=convertView.findViewById(R.id.image_guo);
            viewHolder.img3=convertView.findViewById(R.id.image_zhong);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_banci.setText((String) data.get(position).get("banci"));
        viewHolder.tv_start.setText((String) data.get(position).get("tv_start"));
        viewHolder.tv_over.setText((String)data.get(position).get("tv_end"));
        viewHolder.tv_seat1.setText((String)data.get(position).get("seat1"));
        viewHolder.tv_seat2.setText((String)data.get(position).get("seat2"));
        viewHolder.tv_seat3.setText((String)data.get(position).get("seat3"));
        viewHolder.tv_seat4.setText((String)data.get(position).get("seat4"));
        return convertView;
    }
    public class ViewHolder {
        private TextView tv_banci;
        private TextView tv_start;
        private TextView tv_over;
        private TextView tv_seat1;
        private TextView tv_seat2;
        private TextView tv_seat3;
        private TextView tv_seat4;
        private ImageView img1,img2,img3;
    }
}
