package com.example.abc123.my12306.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abc123.my12306.R;

import androidx.fragment.app.Fragment;

/**
 * Created by 皓哥~_~ on 2020/6/30.
 */

public class TicketFragment extends Fragment {
    public  TicketFragment(){
        //需要空的构造方法
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ticket_fragment,container,false);
    }
}
