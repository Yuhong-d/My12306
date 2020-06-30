package com.example.abc123.my12306;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.example.abc123.my12306.User.UserActivity;

public class MainActivity extends AppCompatActivity {
    private Button bt_login;
    private TextView tv_f;
    private TextView tv_register;
    private EditText et_number,et_password;
    private CheckBox checkBox;
    private MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt_login = findViewById(R.id.bt_login);
        tv_f = findViewById(R.id.tv_f);
        et_number = findViewById(R.id.et_number);
        et_password = findViewById(R.id.et_password);
        checkBox=findViewById(R.id.checkBox);
        checkBox.isChecked();
        tv_register=findViewById(R.id.tv_register);
        myHelper = new MyHelper(MainActivity.this, "Information.db", null, 1);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judge()) {
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
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
    private boolean query() {
        String name = et_number.getText().toString();//用户端输入的账号
        String word = et_password.getText().toString();//用户端输入的密码
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String sql = "select password from Information where username like ?";//查询输入用户名对应密码
        Cursor cursor = db.rawQuery(sql,new String[]{"%" + name + "%"});
        while(cursor.moveToNext()){//判断密码是否正确
            String password = cursor.getString(cursor.getColumnIndex("password"));
            if(word.equals(password))
            {
                new AlertDialog.Builder(MainActivity.this).setMessage("登陆成功").show();
                return true;
            }
        }
        return false;
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

