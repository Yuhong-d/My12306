package com.example.abc123.my12306.User;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abc123.my12306.Fragment.MyFragment;
import com.example.abc123.my12306.R;

import androidx.appcompat.app.AppCompatActivity;

public class my_pwd extends AppCompatActivity {
private EditText password;
private EditText pwd;
private Button save;
Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pwd);
        password=findViewById(R.id.editText);
        pwd=findViewById(R.id.editText2);
        save=findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

}
