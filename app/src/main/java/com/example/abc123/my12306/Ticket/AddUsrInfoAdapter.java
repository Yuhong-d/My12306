package com.example.abc123.my12306.Ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc123.my12306.R;

import java.util.List;
import java.util.Map;

public class AddUsrInfoAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Map<String,String>>datalist;

    public AddUsrInfoAdapter(Context context, List<Map<String,String>>data){
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

        return convertView;
    }
    public class ViewHodler{
        private CheckBox cbx_addusr;
        private TextView tv1,tv2,tv3;
        private ImageView img;
    }
}
