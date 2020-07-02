package com.example.abc123.my12306.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.abc123.my12306.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class TicketFragment extends Fragment {
    private TextView tv_star,tv_end;
    private Button bt_search;
    private SharedPreferences sp;
    private ImageView turn;
    private ImageView three,five;
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
        tv_star=getActivity().findViewById(R.id.tv_start);
        tv_end=getActivity().findViewById(R.id.tv_end);
        bt_search=getActivity().findViewById(R.id.bt_search);
        turn = getActivity().findViewById(R.id.imageView4);
        three=getActivity().findViewById(R.id.imageView3);
        five=getActivity().findViewById(R.id.imageView5);

         three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CityActivity.class);
                startActivity(intent);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CityActivity.class);
                startActivity(intent);
            }
        });

        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1 = tv_star.getText().toString();
                String s2 = tv_end.getText().toString();
                tv_star.setText(s2);
                tv_end.setText(s1);
            }
        });

       bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Ticketone.class);
                startActivity(intent);
            }
        });

    }
}
