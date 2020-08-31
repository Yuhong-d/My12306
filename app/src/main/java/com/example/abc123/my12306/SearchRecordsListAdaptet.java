package com.example.abc123.my12306;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abc123.my12306.Fragment.TicketFragment;
import com.example.abc123.my12306.R;

import java.util.List;

import static android.app.PendingIntent.getActivity;

//使用ListView展示搜索记录列表，创建适配器
public class SearchRecordsListAdaptet extends BaseAdapter {

    private Context context;
    private List<String> searchRecordsList;
    private LayoutInflater inflater;

    public SearchRecordsListAdaptet(Context context, List<String>searchRecordsList ){
        this.context = context;
        this.searchRecordsList = searchRecordsList;
        inflater = LayoutInflater.from(context);
    }




    @Override
    public int getCount() {
        return searchRecordsList.size() == 0 ? 0 : searchRecordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchRecordsList.size() == 0 ? null : searchRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.account_list_itemhis,null);
            viewHolder.account_list_itemhis_tv= (TextView) convertView.findViewById(R.id.historytv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.account_list_itemhis_tv.setText(searchRecordsList.get(position));
        Log.d("ticketfragment","position:"+searchRecordsList.get(position));
        Log.d("ticketfragment","size:"+position);
        return convertView;
    }

    private class ViewHolder {
        public TextView account_list_itemhis_tv;

    }
}

