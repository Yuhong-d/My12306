package com.example.abc123.my12306;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        private Button bt_login;
        private TextView tv_f;
        private TextView tv_register;
        private EditText et_number,et_password;
        private CheckBox checkBox;
        private SharedPreferences sp;
        //    private MyHelper myHelper;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bt_login = findViewById(R.id.bt_login);
            tv_f = findViewById(R.id.tv_f);
            et_number = findViewById(R.id.et_number);
            et_password = findViewById(R.id.et_password);
            checkBox=findViewById(R.id.checkBox);
            tv_register=findViewById(R.id.tv_register);
            sp=this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            if(sp.getBoolean("checkboxBoolean",false)){
                et_number.setText(sp.getString("et_number",null));
                et_password.setText(sp.getString("et_password",null));
                checkBox.setChecked(true);
            }
//        myHelper = new MyHelper(MainActivity.this, "Information.db", null, 1);
            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String myname="dong";
                    String mypwd="dong";
                    String user = et_number.getText().toString().trim();
                    String pswd = et_password.getText().toString().trim();
                    if (judge() && user.equals(myname) && pswd.equals(mypwd)) {
                        startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));
                    }else{
                        Toast.makeText(MainActivity.this, "密码或用户名出现错误",
                                Toast.LENGTH_SHORT).show();
                    }
                    boolean CheckBoxLogin=checkBox.isChecked();
                    if(CheckBoxLogin){
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("et_number",user);
                        editor.putString("et_password",pswd);
                        editor.putBoolean("checkboxBoolean",true);
                        editor.commit();
                    }else{
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("et_number",null);
                        editor.putString("et_password",null);
                        editor.putBoolean("checkboxBoolean",false);
                        editor.commit();
                    }

                }
            });
            tv_f.setOnClickListener(new View.OnClickListener() {
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

            if (TextUtils.isEmpty(et_number.getText().toString()) ) {
                Toast.makeText(MainActivity.this, "用户名不能为空",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(et_password.getText().toString())) {
                Toast.makeText(MainActivity.this, "密码不能为空",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }
