package com.example.abc123.my12306;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.Fragment.MyFragment;
import com.example.abc123.my12306.Fragment.MyFragmentPagerAdapter;
import com.example.abc123.my12306.Fragment.OrderFragment;
import com.example.abc123.my12306.Fragment.TicketFragment;

import java.util.ArrayList;

public class ViewPagerActivity extends FragmentActivity {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private ImageView image;
    private TextView view1, view2, view3;
    private int currIndex;//当前页卡编号
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        InitTextView();
        InitImage();
        InitViewPager();
    }
    public void InitTextView(){
        view1 = (TextView)findViewById(R.id.tv_guid1);
        view2 = (TextView)findViewById(R.id.tv_guid2);
        view3 = (TextView)findViewById(R.id.tv_guid3);

        view1.setOnClickListener(new txListener(0));
        view2.setOnClickListener(new txListener(1));
        view3.setOnClickListener(new txListener(2));
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
        }
    }

    public void InitImage(){
        image = (ImageView)findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW/3 - bmpW)/2;

        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        image.setImageMatrix(matrix);
    }

    public void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        //数据源
        fragmentList = new ArrayList<Fragment>();
        TicketFragment ticketFragment = new TicketFragment();
        OrderFragment orderFragment = new OrderFragment();
        MyFragment myFragment = new MyFragment();
        fragmentList.add(ticketFragment);
        fragmentList.add(orderFragment);
        fragmentList.add(myFragment);

        //给ViewPager设置适配器
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.addOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
        Intent intent = getIntent();
        if (intent != null) {
            int page = intent.getIntExtra("page", 0);//显示标签页第一页
            mPager.setCurrentItem(page);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset *2 +bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageSelected(int positon) {
            // 页卡切换时的动画效果，ViewAnimation
            Animation animation = new TranslateAnimation(currIndex*one,positon*one,0,0);//平移动画
            currIndex = positon;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            image.startAnimation(animation);//是用ImageView来显示动画的
            int i = currIndex + 1;
//            Toast.makeText(ViewPagerActivity.this, "您选择了第"+i+"个页卡", Toast.LENGTH_SHORT).show();
        }
    }

}
