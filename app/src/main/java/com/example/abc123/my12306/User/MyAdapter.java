package com.example.abc123.my12306.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MyAdapter extends SimpleAdapter {
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    private Context mcontext;
    private List<? extends Map<String, String>>list;
    private LayoutInflater inflater;
    final int VIEW_TYPE=2;
    final int TYPE_1=0;
    final int TYPE_2=1;

    public MyAdapter(Context context, List<? extends Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mcontext=context;
        list=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position<4)
            return TYPE_1;
        else
            return TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        int type=getItemViewType(position);
        if (convertView==null){
            switch (type){
                case TYPE_1:
                    convertView=inflater.inflate(R.layout.account_list_item_2,parent,false);
                    holder=new ViewHolder();
                    holder.tv_attribute=(TextView)convertView.findViewById(R.id.attribute);
                    holder.tv_value=(TextView)convertView.findViewById(R.id.value);
                    holder.img=(ImageView)convertView.findViewById(R.id.img);
                    convertView.setTag(holder);
                    break;
                case TYPE_2:
                    convertView=inflater.inflate(R.layout.account_list_item_2,parent,false);
                    holder=new ViewHolder();
                    holder.tv_attribute=(TextView)convertView.findViewById(R.id.attribute);
                    holder.tv_value=(TextView)convertView.findViewById(R.id.value);
                    holder.img=(ImageView)convertView.findViewById(R.id.img);
                    holder.img.setVisibility(View.VISIBLE);
                    convertView.setTag(holder);
                    break;
            }
        }else{
            switch (type){
                case TYPE_1:
                    holder=(ViewHolder)convertView.getTag();
                    break;
                case TYPE_2:
                    holder=(ViewHolder)convertView.getTag();
                    break;
            }
        }
        //设置资源
        holder.tv_attribute.setText(list.get(position).get("attribute"));
        holder.tv_value.setText(list.get(position).get("value"));

        return convertView;
    }

    class ViewHolder{
        TextView tv_attribute,tv_value;
        ImageView img;
    }
}
