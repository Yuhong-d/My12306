package com.example.abc123.my12306.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abc123.my12306.R;
import java.util.LinkedList;

public class Myadapter extends BaseAdapter {
    private Context mContext;
    private LinkedList<Data> mData;
    public Myadapter(){}
    public Myadapter(LinkedList<Data> mData,Context mContext){
        this.mData=mData;
        this.mContext=mContext;
    }


    public Myadapter(LinkedList<String> mData, TicketFragment mContext) {
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.account_list_itemhis,parent,false);
            holder = new ViewHolder();
            holder.tv_content= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(mData.get(position).getContent());
        return convertView;
    }
    private class ViewHolder{
        TextView tv_content;
    }

}

