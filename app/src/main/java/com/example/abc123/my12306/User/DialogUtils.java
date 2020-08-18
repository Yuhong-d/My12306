package com.example.abc123.my12306.User;

import android.content.DialogInterface;

import java.lang.reflect.Field;

//TODO 设置dialog对话框是否关闭
public class DialogUtils {
public static void setClosable(DialogInterface dialog, boolean b){
try {
  Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
  field.setAccessible(true);
  field.set(dialog,b);
} catch (NoSuchFieldException e) {
  e.printStackTrace();
} catch (IllegalAccessException e) {
  e.printStackTrace();
} catch (IllegalArgumentException e){
  e.printStackTrace();
}
}
}
