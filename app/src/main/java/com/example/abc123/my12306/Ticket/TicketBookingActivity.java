package com.example.abc123.my12306.Ticket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.my_detailcontact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketBookingActivity extends AppCompatActivity {

    private TextView tv_addhuman,tv_submit;//添加乘客
    private ListView listView;
    private List<Map<String,Object>> data;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tvPrice,tvSeat,tvTotal;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ticket_booking);

        tv1=findViewById(R.id.starpl);
        tv2=findViewById(R.id.endpl);
        tv3=findViewById(R.id.ticknum);
        tv4=findViewById(R.id.ticktime);
        tv5=findViewById(R.id.startime);
        tv6=findViewById(R.id.endtime);
        tvSeat=findViewById(R.id.tickleft);
        tvPrice=findViewById(R.id.tvPrice);//单价
        tvTotal=findViewById(R.id.tvTotal);//订单总额
        Intent intent=getIntent();
        Map<String,Object> map= (Map<String, Object>) intent.getSerializableExtra("dataMap");
        tv1.setText(map.get("fromStationName").toString());
        tv2.setText(map.get("toStationName").toString());
        tv3.setText(map.get("banci").toString());
        tv4.setText(map.get("startTrainDate").toString()+"("+map.get("tv_end").toString().split("\\(")[1]);
        tv5.setText(map.get("tv_start").toString());
        tv6.setText(map.get("tv_end").toString().split("\\(")[0]);
        tvSeat.setText(intent.getStringExtra("seat")+"("+intent.getStringExtra("ticketNum")+"张)");
        tvPrice.setText(intent.getStringExtra("price"));
        listView=findViewById(R.id.ls);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int j, long id) {
                new androidx.appcompat.app.AlertDialog.Builder(TicketBookingActivity.this)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setTitle("提示")
                        .setMessage("确定删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                double price= Double.parseDouble(tvPrice.getText().toString());
                                data.remove(j);
                                adapter.notifyDataSetChanged();
                                tvTotal.setText("订单总额：￥"+price*data.size());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
                return false;
            }
        });
                tv_addhuman = findViewById(R.id.tv_addhuman);
                tv_addhuman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TicketBookingActivity.this, AddUserInfo.class);
                        startActivityForResult(intent, 110);
                    }
                });
                tv_submit = findViewById(R.id.tv_submit);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TicketBookingActivity.this, TicketSubmitActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });


            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent d) {
                super.onActivityResult(requestCode, resultCode, d);
                if (resultCode == 404) {
                    Toast.makeText(TicketBookingActivity.this, "未选择乘车人", Toast.LENGTH_SHORT);
                    return;
                }
                switch (requestCode) {
                    case 110:
                        double price= Double.parseDouble(tvPrice.getText().toString());
                        data = (List<Map<String, Object>>) d.getSerializableExtra("data");
                        adapter = new MyAdapter(TicketBookingActivity.this, data);
                        listView.setAdapter(adapter);
                        tvTotal.setText("订单总额：￥"+price*data.size());
                        break;
                }
            }

            private class MyAdapter extends BaseAdapter {
                private Context mcontext;
                private List<Map<String, Object>> datalist;


                public MyAdapter(Context context, List<Map<String, Object>> data) {
                    mcontext = context;
                    datalist = data;
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
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHodler viewHodler = null;
                    if (convertView == null) {
                        viewHodler = new ViewHodler();
                        convertView = LayoutInflater.from(mcontext).inflate(R.layout.ticket_addusr_list_item, null);
                        viewHodler.tv1 = convertView.findViewById(R.id.tvName);
                        viewHodler.tv2 = convertView.findViewById(R.id.tvIdCard);
                        viewHodler.tv3 = convertView.findViewById(R.id.tvTel);
                        viewHodler.cbx_addusr = convertView.findViewById(R.id.cbx_addusr);
                        viewHodler.img = convertView.findViewById(R.id.img);
                        convertView.setTag(viewHodler);
                    } else {
                        viewHodler = (ViewHodler) convertView.getTag();
                    }
                    viewHodler.tv1.setText(datalist.get(position).get("name").toString());
                    viewHodler.tv2.setText(datalist.get(position).get("idcard").toString());
                    viewHodler.tv3.setText(datalist.get(position).get("num").toString());
                    viewHodler.cbx_addusr.setVisibility(View.GONE);
                    viewHodler.img.setImageResource(R.drawable.cancel_25);
                    viewHodler.tv1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TicketBookingActivity.this);
                            builder.setTitle("提醒！");
                            builder.setMessage("确定要删除该乘客吗？");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            return false;
                        }
                    });
                    return convertView;
                }

                public class ViewHodler {
                    private CheckBox cbx_addusr;
                    private TextView tv1, tv2, tv3;
                    private ImageView img;
                }
            }


}
