package com.example.abc123.my12306;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.ConstantCallSite;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.os.Build.HOST;
import static androidx.constraintlayout.solver.SolverVariable.Type.CONSTANT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
        private Button bt_login;
        private TextView tv_forget;
        private TextView tv_register;
        private EditText edtusername,edtpassword;
        private CheckBox checkBox;
        private SharedPreferences sp;
        private Handler handler;
        //    private MyHelper myHelper;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bt_login = findViewById(R.id.bt_login);
            tv_forget = findViewById(R.id.tv_f);
            edtusername = findViewById(R.id.et_usrname);
            edtpassword = findViewById(R.id.et_password);
            checkBox=findViewById(R.id.checkBox);
            tv_register=findViewById(R.id.tv_register);
            sp=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            edtusername.setText(sp.getString("et_username",""));
            edtpassword.setText(sp.getString("et_password",""));
            handler=new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            int result=msg.arg1;
                            String sessionId=msg.obj.toString();
                            if (result==0){
                                //用户名或密码错误
                               edtusername.setError("用户名或密码错误!");
                               edtusername.requestFocus();
                            }else if (result==1){
                                SharedPreferences.Editor editor=sp.edit();
                                if (checkBox.isChecked()){
                                    editor.putString("et_username",edtusername.getText().toString());
                                    editor.putString("et_password",edtpassword.getText().toString());
                                }else{
                                    editor.clear();
                                }
                                //否则
                                editor.putString("cookie",sessionId);
                                editor.commit();

                                Intent intent=new Intent(MainActivity.this, ViewPagerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("sessionId",sessionId);
                                Log.d(TAG, "session： " + sessionId);
                                startActivity(intent);
                                //MainActivity.this.finish();
                            }
                    }
                }
            };
//        myHelper = new MyHelper(MainActivity.this, "Information.db", null, 1);
            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (judge()) {
                        //检查网络状态
                        if(!NetUtils.check(MainActivity.this)){
                            Toast.makeText(MainActivity.this, "网络异常，请检查！",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                Message msg = handler.obtainMessage();
                                String result = "";
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("username", edtusername.getText().toString())
                                        .add("password", Md5Utils.MD5(edtpassword.getText().toString()))
                                        .build();
                                Request request = new Request.Builder()
                                        .url("http://192.168.1.3:8080/My12306/Login ").post(requestBody).build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseData = response.body().string();
                                    Log.d(TAG, "获取的服务器数据： "+responseData);
                                    if (response.isSuccessful()) {
                                        //解析数据，Pull
                                        XmlPullParser parser = Xml.newPullParser();
                                        //parser.setInput(response.body().byteStream(),"UTF-8");
                                        parser.setInput(new StringReader(responseData));
                                        int type = parser.getEventType();
                                        while (type != XmlPullParser.END_DOCUMENT) {
                                            switch (type) {
                                                case XmlPullParser.START_TAG:
                                                    if ("result".equals(parser.getName())) {
                                                        result = parser.nextText();
                                                    }
                                                    break;
                                            }
                                            type = parser.next();
                                        }
                                        //读取sessionID
                                        Headers headers = response.headers();
                                        Log.d(TAG, "headers: "+headers );
                                        List<String> cookies = headers.values("Set-Cookie");
                                        String session = cookies.get(0);
                                        String sessionId = session.substring(0, session.indexOf(";"));
                                        msg.what = 1;
                                        msg.arg1 = Integer.parseInt(result);
                                        msg.obj = sessionId;
                                    } else {
                                        msg.what = 2;
                                    }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                    msg.what=2;
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                    msg.what=2;
                                }
                                handler.sendMessage(msg);
                            }
                        }.start();
                    }


                }
            });
            tv_forget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //注册
            tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "注册", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //判断登录信息是否合法
        private boolean judge() {

            if (TextUtils.isEmpty(edtusername.getText().toString()) ) {
                Toast.makeText(MainActivity.this, "用户名不能为空",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(edtpassword.getText().toString())) {
                Toast.makeText(MainActivity.this, "密码不能为空",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }
