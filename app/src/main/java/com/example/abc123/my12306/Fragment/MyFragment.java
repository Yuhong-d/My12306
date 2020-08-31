package com.example.abc123.my12306.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abc123.my12306.MainActivity;
import com.example.abc123.my12306.NetUtils;
import com.example.abc123.my12306.R;
import com.example.abc123.my12306.User.my_account;
import com.example.abc123.my12306.User.my_addcontact;
import com.example.abc123.my12306.User.my_contact;
import com.example.abc123.my12306.User.my_pwd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyFragment extends Fragment {
    private static String TAG="Password";
    private ListView listView;
    private Button btn;//退出
    // 定义数组
    private String[] data = {"我的联系人","我的账户","我的密码"};
    private SharedPreferences sp;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (progressDialog != null){
                progressDialog.dismiss();
            }
            switch (msg.what){

                case 1:
                    String re1 =msg.obj.toString();
                    if ("1".equals(re1) ){
//
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }else if ("0".equals(re1)){
                        Toast.makeText(getActivity(),"退出失败！",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    break;
            }
        }
    };

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listView);
        btn = view.findViewById(R.id.button);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_item, R.id.texttime, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (data[position]) {
                    case "我的联系人":
                        intent.setClass(getActivity(), my_contact.class);
                        startActivity(intent);
                        break;
                    case "我的账户":
                        intent.setClass(getActivity(), my_account.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case "我的密码":
                        layDialog();
                        break;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtils.check(getActivity())) {
                    Toast.makeText(getActivity(), "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(
                        getActivity(),
                        null,
                        "正在退出中....",
                        false,true);
                    new Thread() {
                        @Override
                        public void run() {
                            // super.run();
                            Message msg = handler.obtainMessage();
                            OkHttpClient client = new OkHttpClient();
                            //获取sessionId
                            sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                            String sessionId = sp.getString("cookie", "");
                            Log.d(TAG, "session： " + sessionId);
                            //建立请求
                            RequestBody requestBody = new FormBody.Builder()
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://10.0.2.2:8080/My12306/otn/Logout")
                                    .addHeader("cookie", sessionId)
                                    .post(requestBody)
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String responsedata = response.body().string();
                                Log.d(TAG, "获取的服务器数据： " + responsedata);
                                if (response.isSuccessful()) {

                                    //    JSONObject jsonObject=new JSONObject(responsedata);
                                    Gson gson = new GsonBuilder().create();
                                    String resultString = gson.fromJson(responsedata, String.class);
                                    msg.obj = resultString;
                                    msg.what = 1;
                                } else {
                                    msg.what = 2;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                msg.what = 2;
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                msg.what = 3;
                            }
                            handler.sendMessage(msg);
                        }
                    }.start();
                }
                // getActivity().finish();
        });
    }
    private void layDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity() );
        //创建view，并将布局加入view中
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.number_dialog,null,false);
        //将view加入builder
        builder.setView(view).setTitle("请输入原密码：");
        //创建dialog
        final Dialog dialog=builder.create();
        //初始化控件
        final EditText edt_number=(EditText) view.findViewById(R.id.edt_number);
        final Button confirm=(Button)view.findViewById(R.id.btnyes);
        final Button cancel=(Button)view.findViewById(R.id.btnno);
        //设置Button的事件和内容
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetUtils.check(getContext())){
                    Toast.makeText(getContext(), "网络异常，请检查！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        OkHttpClient client=new OkHttpClient();
                        Message msg=handler.obtainMessage();
                        String result="";
                        sp=getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        String sessionId =sp.getString("cookie","");
                        RequestBody requestBody=new FormBody.Builder()
                                .add("oldPassword",edt_number.getText().toString())
                                .add("action","query")
                                .build();
                        Request request=new Request.Builder()
                                .url("http://10.0.2.2:8080/My12306/otn/AccountPassword")
                                .addHeader("cookie",sessionId)
                                .post(requestBody)
                                .build();
                        try {
                            Response response=client.newCall(request).execute();
                            String responsedata=response.body().string();
                            Log.d(TAG, "run: "+responsedata);
                            if (response.isSuccessful()){
                                msg.what=1;
                                msg.arg1=Integer.parseInt(responsedata.substring(1,2));
                                msg.obj=edt_number.getText().toString();
                            }else {
                                msg.what=2;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.what=2;
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
                 handler=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d(TAG, "handleMessage: "+msg.what+"");
                        switch (msg.what){
                            case 1:
                                if (msg.arg1==1){
                                    Intent intent=new Intent();
                                    intent.setClass(getActivity(), my_pwd.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("oldPassword",msg.obj.toString());
                                    startActivity(intent);
                                    dialog.dismiss();
                                }else if(msg.arg1==0){
                                    edt_number.setError("原密码错误，请重新输入！");
                                }else {
                                    Toast.makeText(getContext(),"网络故障！",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                Log.d(TAG, "result: "+msg.arg1+"");
                                Toast.makeText(getContext(),"网络故障！",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
