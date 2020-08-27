package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc123.my12306.Order.PaidActivity;
import com.example.abc123.my12306.Order.Unpaid;
import com.example.abc123.my12306.Order.UnpaidActivity;
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

public class PaidSuccessActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private Button button;
    private List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_success);
        textView=findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView);
        button=findViewById(R.id.btn);
        Intent intent=getIntent();
        //textView.setText("您的订单"+intent.getStringExtra("num")+"支付成功，可以凭此二维码办理取票业务，也可以在订单中查看相关信息以及二维码");
        dataList= new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("name","孔乙己");
        map.put("train",intent.getStringExtra("train"));
        map.put("time",intent.getStringExtra("time"));
        map.put("carriage","2车10号");
        dataList.add(map);
        StringBuffer s=new StringBuffer();
        for (int i=0;i<dataList.size();i++){
            Map<String,Object> map1=dataList.get(i);
            s.append(map1.get("name")+","+map1.get("train")+","+map1.get("time")+","+map1.get("carriage")+"\n");
        }
        Bitmap bitmap=createQRCodeBitmap(s.toString(),500,500,"UTF-8",
                "H","1");
        imageView.setImageBitmap(bitmap);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PaidSuccessActivity.this,Unpaid.instance.getActivity().getClass());
                intent1.putExtra("page",1);
                startActivity(intent1);
                finish();
            }
        });
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
