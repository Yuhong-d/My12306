package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.abc123.my12306.R;
import java.util.List;
import java.util.Map;

public class Ticketadapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;

    public Ticketadapter(Context context, List<Map<String, Object>> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.tickettwo_item,null);
            viewHolder.tv_seattype = convertView.findViewById(R.id.tv_seattype);
            viewHolder.tv_ticket = convertView.findViewById(R.id.tv_ticket);
            viewHolder.tv_price = convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_seattype.setText((String) data.get(position).get("seattype"));
        viewHolder.tv_ticket.setText((String) data.get(position).get("ticket"));
        viewHolder.tv_price.setText((String)data.get(position).get("price"));
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_seattype;
        private TextView tv_ticket;
        private TextView tv_price;
    }
}