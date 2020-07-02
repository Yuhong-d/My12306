package com.example.abc123.my12306.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abc123.my12306.R;

import androidx.fragment.app.Fragment;

/**
 * Created by 皓哥~_~ on 2018/7/4.
 */

public class OrderFragment extends Fragment {
    public OrderFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_order_fragment,container,false);
    }
}
