package com.example.abc123.my12306.User;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class my_pwd extends Dialog implements View.OnClickListener{
private EditText password;
private EditText pwd;
private Button save;
Context context;
public my_pwd(Context context){
    super(context);
    this.context=context;
}
public my_pwd(Context context, int theme){
    super(context);
    this.context=context;
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pwd);
        password=findViewById(R.id.editText);
        pwd=findViewById(R.id.editText2);
        save=findViewById(R.id.button2);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
