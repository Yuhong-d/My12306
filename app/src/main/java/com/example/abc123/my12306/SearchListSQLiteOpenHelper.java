package com.example.abc123.my12306;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchListSQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "keep.db";
    private final static int DB_VERSION = 1;
    private Object context;


    public SearchListSQLiteOpenHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DB_NAME, null, DB_VERSION);
        super(context,DB_NAME,null, DB_VERSION);
         /*
        context:  上下文
        name:数据库的名称
        factory：游标工厂
        version：数据库的版本
        */
    }


//    public SearchListSQLiteOpenHelper(TicketFragment context) {
//        super(context,DB_NAME,null, DB_VERSION);
//    }
    //此方法用来建表，系统自动回调该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS building (_id INTEGER PRIMARY KEY AUTOINCREMENT, placename TEXT);";
        db.execSQL(sqlStr);
    }

    //这个方法在数据库升级的时候自动回调这个方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clear(SQLiteDatabase db){
        String sqlc="DELETE FROM building;";
        db.execSQL(sqlc);

    }
}
