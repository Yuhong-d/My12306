package com.example.abc123.my12306.Fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc123.my12306.R;
import com.example.abc123.my12306.Ticket.CityActivity;
import com.example.abc123.my12306.Ticket.Ticketone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class TicketFragment extends Fragment {
    private static final String PRE_SEARCH_HISTORY = "pre_search_history";
    private static final String SEARCH_HISTORY = "search_history";
    private TextView tv_star,tv_end;
    private TextView tv_texttime;
    private TextView tv_time;
    private Button bt_search;
    private TextView history;
    private ListView listView;
    private ArrayAdapter<String> mArrAdapter;
    private List<String> mHistoryKeywords;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ImageView turn;
    private ArrayAdapter<String> arrayAdapter;
    private Myadapter mAdapter;
    private List<String> mData;
    private TicketFragment mContext;
    public  TicketFragment(){
        //需要空的构造方法
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ticket_fragment,container,false);
    }
     @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_star=view.findViewById(R.id.tv_start);
        tv_end=view.findViewById(R.id.tv_end);
         tv_texttime=view.findViewById(R.id.texttime);
         tv_time=view.findViewById(R.id.time);
         Calendar rightNow = Calendar.getInstance();
         tv_time.setText(rightNow.get(Calendar.YEAR)+"年"+(rightNow.get(Calendar.MONTH)+1)+"月"+rightNow.get(Calendar.DATE)+"日");
         //history=view.findViewById(R.id.his);
         listView=view.findViewById(R.id.listView);
        bt_search=view.findViewById(R.id.bt_search);
        turn = view.findViewById(R.id.turn);
         ImageView turn = getActivity().findViewById(R.id.turn);
         turn.setImageResource(R.drawable.turn);
//         turn.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//              String left=tv_star.getText().toString().trim();
//              String right=tv_end.getText().toString().trim();
//              tv_star.setText("right");
//              tv_end.setText("left");
//             }
//         });
         //待解决
         String first=tv_star.getText().toString().trim();
         String last=tv_end.getText().toString().trim();
         mHistoryKeywords=new ArrayList<String>();
         mHistoryKeywords.add(first+"—>"+last);
         arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.account_list_itemhis,R.id.textView,mHistoryKeywords);
         listView.setAdapter(arrayAdapter);
         //城市导航
         tv_star.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), CityActivity.class);
                 startActivityForResult(intent,110);
             }
         });
         tv_end.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), CityActivity.class);
                 startActivityForResult(intent,120);
             }
         });

         turn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String s1 = tv_star.getText().toString();
                 String s2 = tv_end.getText().toString();
                 tv_star.setText(s2);
                 tv_end.setText(s1);
                 Log.d("Ticket", "onClick: "+s1+s2);
             }
         });

         tv_time.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View v) {
                 Calendar c = Calendar.getInstance(Locale.CHINA);

                 new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                     @Override
                     public void onDateSet(DatePicker view, int year,

                                           int monthOfYear, int dayOfMonth) {

                         Calendar c = Calendar.getInstance();
                         c.set(year, monthOfYear, dayOfMonth);

                         String strFormat = "yyyy年MM月dd日";  //格式设定

                         SimpleDateFormat sdf = new SimpleDateFormat(strFormat, Locale.CHINA);

                         tv_time.setText(sdf.format(c.getTime())); //设置日期
                     }

                 }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
             }

         });
         //无法刷新数据
         bt_search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String s1 = tv_star.getText().toString();
                 String s2 = tv_end.getText().toString();
                 Intent intent = new Intent(getActivity(), Ticketone.class);
                 intent.putExtra("start",s1);
                 intent.putExtra("end",s2);
                 startActivity(intent);
             }
         });


     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String s=data.getStringExtra("city");
        switch (requestCode){
            case 110:
                tv_star.setText(s);
                break;
            case 120:
               tv_end.setText(s);
               break;
        }
    }
    public void AddNewData(){
        String first=tv_star.getText().toString().trim();
        String last=tv_end.getText().toString().trim();
        mHistoryKeywords.add(first+"—>"+last);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.account_list_itemhis,R.id.textView,mHistoryKeywords);
        listView.setAdapter(arrayAdapter);
    }
//    private void updateListItem(int postion,Data mData){
//        int visiblePosition = listView.getFirstVisiblePosition();
//        View v =listView.getChildAt(postion - visiblePosition);
//        TextView tv = (TextView) v.findViewById(R.id.textView);
//        tv.setText(mData.getContent());
//    }
}
