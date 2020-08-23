package com.example.abc123.my12306.User;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abc123.my12306.Fragment.MyFragment;
import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class my_pwd extends AppCompatActivity {
    private EditText pwd_new1,pwd_new2;
    private Button save;
    Context context;
    private SharedPreferences sp;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.arg1==1){
                        Toast.makeText(my_pwd.this,"保存成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else if(msg.arg1==0){
                        Toast.makeText(my_pwd.this,"保存失败！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(my_pwd.this,"网络故障！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(my_pwd.this,"网络故障！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pwd);
        pwd_new1=findViewById(R.id.editText);
        pwd_new2=findViewById(R.id.editText2);
        Intent intent=getIntent();
        final String oldPassword=intent.getStringExtra("oldPassword");
        save=findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //两次密码是否一致
                if (pwd_new1.getText().toString().equals(pwd_new2.getText().toString())){
                    if (pwd_new1.equals(oldPassword)){
                        Toast.makeText(my_pwd.this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        if(!NetUtils.check(my_pwd.this)){
                            Toast.makeText(my_pwd.this, "网络异常，请检查！",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                OkHttpClient client=new OkHttpClient();
                                Message message=handler.obtainMessage();
                                sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                String sessionId =sp.getString("cookie","");
                                RequestBody requestBody=new FormBody.Builder()
                                        .add("newPassword",pwd_new1.getText().toString())
                                        .add("action","update")
                                        .build();
                                Request request=new Request.Builder()
                                        .url("http://10.0.2.2:8080/My12306/otn/AccountPassword")
                                        .addHeader("cookie",sessionId)
                                        .post(requestBody)
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    if (response.isSuccessful()){
                                        String responsedata=response.body().string();
                                        message.what=1;
                                        message.arg1=Integer.parseInt(responsedata.substring(1,2));
                                    }else {
                                        message.what=2;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    message.what=2;
                                }
                                handler.sendMessage(message);
                            }
                        }.start();
                    }
                }else{
                    pwd_new2.setError("两次密码不一致！");
                }
            }
        });
    }

}
