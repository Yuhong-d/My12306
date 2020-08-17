package com.example.abc123.my12306.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Unpaid extends Fragment {
    private ListView ls;
    private List<Map<String, String>> dataList;
    public Unpaid(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_layout_unpaid,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ls=view.findViewById(R.id.ls);
        dataList=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<>();
        map.put("num","2014yH");
        map.put("type","待支付");
        map.put("train","G011");
        map.put("time","2016-4-5");
        map.put("end","北京->上海");
        map.put("person","1人");
        map.put("price","258.0");
        dataList.add(map);
        OrderAdapter orderAdapter=new OrderAdapter(getContext(),dataList);
        ls.setAdapter(orderAdapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
