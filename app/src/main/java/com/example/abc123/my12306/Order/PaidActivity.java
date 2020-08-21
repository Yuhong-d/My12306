package com.example.abc123.my12306.Order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PaidActivity extends AppCompatActivity {
    private ListView ls;
    private List<Map<String, String>> dataList;
    private TextView tv;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);
        ls=findViewById(R.id.ls);
        tv=findViewById(R.id.tv_ordernum);
        button=findViewById(R.id.button);
        Intent intent=getIntent();
        tv.setText(intent.getStringExtra("num"));
        dataList=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<>();
        map.put("name","孔乙己");
        map.put("train",intent.getStringExtra("train"));
        map.put("time",intent.getStringExtra("time"));
        map.put("carriage","2车10号");
        dataList.add(map);
        OrderActivityAdapter activityAdapter=new OrderActivityAdapter(PaidActivity.this,dataList);
        ls.setAdapter(activityAdapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listDialog();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDialog();
            }
        });
    }
    private void listDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(PaidActivity.this);
        final String[] items={"退票","改签"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PaidActivity.this,items[which],Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
    private void layDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(PaidActivity.this);
        //创建view，并将布局加入view中
        View view= LayoutInflater.from(PaidActivity.this).inflate(R.layout.layout_zxing,null,false);
        //将view加入builder
        builder.setView(view);
        //创建dialog
        final Dialog dialog=builder.create();
        //初始化控件
        final TextView edt=(TextView) view.findViewById(R.id.preday);
        Button confirm=(Button)view.findViewById(R.id.button);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        StringBuffer s=new StringBuffer();
        for (int i=0;i<dataList.size();i++){
            Map<String,String> map=dataList.get(i);
            s.append(map.get("name")+","+map.get("train")+","+map.get("time")+","+map.get("carriage"));
        }
        Bitmap bitmap=createQRCodeBitmap(s.toString(),1000,1000,"UTF-8",
                "H","1");
        imageView.setImageBitmap(bitmap);
        //设置Button的事件和内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
//     * @param color_black            黑色色块
//     * @param color_white            白色色块
     * @return BitMap
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            String character_set, String error_correction_level,
                                            String margin) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] =  0xff000000;//黑色色块像素设置
                    } else {
                        pixels[y * width + x] = 0xffffffff;// 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
