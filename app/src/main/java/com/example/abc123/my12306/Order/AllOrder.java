package com.example.abc123.my12306.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.abc123.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AllOrder extends Fragment {
    private ListView ls;
    private List<Map<String, String>> dataList;
    public AllOrder(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_layout_all,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ls=view.findViewById(R.id.ls);
        dataList=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<>();
        map.put("num","201349zH");
        map.put("type","已支付");
        map.put("train","G109");
        map.put("time","2016-4-5");
        map.put("end","北京->上海");
        map.put("person","2人");
        map.put("price","456.0");
        dataList.add(map);
        Map<String,String> map1=new HashMap<>();
        map1.put("num","2014yH");
        map1.put("type","待支付");
        map1.put("train","G011");
        map1.put("time","2016-4-5");
        map1.put("end","北京->上海");
        map1.put("person","1人");
        map1.put("price","258.0");
        dataList.add(map1);
        Map<String,String> map2=new HashMap<>();
        map2.put("num","ddj014jdc");
        map2.put("type","已取消");
        map2.put("train","G202");
        map2.put("time","2016-4-9");
        map2.put("end","北京->上海");
        map2.put("person","1人");
        map2.put("price","258.0");
        dataList.add(map2);
        OrderAdapter orderAdapter=new OrderAdapter(getContext(),dataList);
        ls.setAdapter(orderAdapter);

    }
}
