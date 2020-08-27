package com.example.abc123.my12306.Ticket;

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

public class TicketResult2Adapter extends BaseAdapter {
   private Context context;
   private List<Map<String, Object>> datalist;
   private LayoutInflater inflater;
   public TicketResult2Adapter(Context context, List<Map<String, Object>> datalist){
       this.context = context;
       this.datalist = datalist;
       inflater = LayoutInflater.from(context);
   }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder hh = null;
       if (hh == null){
           hh = new ViewHolder();
           view = inflater.inflate(R.layout.item_ticket_information3,null);
           hh.tvTicketPassengerName3 = view.findViewById(R.id.tvTicketPassengerName3);
           hh.tvTicketPassengerIdCard3 = view.findViewById(R.id.tvTicketPassengerIdCard3);
           hh.tvTicketPassengerTel3 = view.findViewById(R.id.tvTicketPassengerTel3);
           hh.imgTicketPassengerDel3 = view.findViewById(R.id.imgTicketPassengerDel3);
           view.setTag(hh);
       }else {
           hh = (ViewHolder) view.getTag();
       }

       hh.tvTicketPassengerName3.setText(datalist.get(i).get("name").toString());
       hh.tvTicketPassengerIdCard3.setText(datalist.get(i).get("idCard").toString());
       hh.tvTicketPassengerTel3.setText(datalist.get(i).get("tel").toString());
        return view;
    }
    class ViewHolder{
        TextView tvTicketPassengerName3;
        TextView tvTicketPassengerIdCard3;
        TextView tvTicketPassengerTel3;
        ImageView imgTicketPassengerDel3;

    }
}
