package com.example.abc123.my12306.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc123.my12306.Order.AllOrder;
import com.example.abc123.my12306.Order.Unpaid;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.ViewPagerActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class OrderFragment extends Fragment {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private ImageView image;
    private TextView view1, view2;
    public OrderFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_order_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view1=view.findViewById(R.id.tv_unpaid);
        view2=view.findViewById(R.id.tv_all_order);
        view1.setOnClickListener(new txListener(0));
        view2.setOnClickListener(new txListener(1));
        mPager = view.findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        Unpaid unpaid_fra=new Unpaid();
        AllOrder allOrder_fra=new AllOrder();
        fragmentList.add(unpaid_fra);
        fragmentList.add(allOrder_fra);

        //给ViewPager设置适配器
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        view1.setEnabled(false);
        view2.setEnabled(true);
    }

    /**
     * 接收TextView对应的编号，通知ViewPager切换Fragment
     */
    public class txListener implements View.OnClickListener{
        //默认是显示第1个Fragment
        private int index=0;

        public txListener(int i) {
            index =i;
        }
        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            switch (index){
                case 0:
                    view1.setEnabled(false);
                    view2.setEnabled(true);
                    break;
                case 1:
                    view1.setEnabled(true);
                    view2.setEnabled(false);
                    break;
            }
        }
    }



}
