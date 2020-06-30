package com.example.abc123.my12306;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**

 *  数据库

 */
public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public void onCreate(SQLiteDatabase db) {//新建一个表
        String sql = "create table if not exists Information" + "(_id integer primary key autoincrement,username text,password text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Information");
        onCreate(db);
    }

}