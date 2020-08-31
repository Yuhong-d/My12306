package com.example.abc123.my12306;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


//创建数据库操作类SearchListDbOperation ,用于对数据进行，增删查等操作
public class SearchListDbOperation {
    private SearchListSQLiteOpenHelper searchListSQLiteOpenHelper;
    private SQLiteDatabase recordsDb;
    private String tableName;
    public SearchListDbOperation(Context context, String tableName){
        searchListSQLiteOpenHelper = new SearchListSQLiteOpenHelper(context,"keep.db",null,1);
        this.tableName = tableName;
    }


    //添加查询记录
    public void addRecords(String record){
        if(!isHasRecord(record)){
            ContentValues values = new ContentValues();
            values.put("placename", record);
            //添加
            recordsDb.insert(tableName, null, values);//表名，插入条件，增添数据；
            //关闭
            recordsDb.close();
        }
    }

    //判断是否有该记录,在上个添加查询时先进行判断
    public boolean isHasRecord(String record){
        boolean isHasRecord = false;
        recordsDb = searchListSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("placename")))) {

                isHasRecord = true;
            }
        }
        return isHasRecord;
    }

    //获取全部搜索记录
    public List<String> getRecordsList() {
        List<String> recordsList = new ArrayList<>();
        recordsDb = searchListSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String placestarend = cursor.getString(cursor.getColumnIndexOrThrow("placename"));
            recordsList.add(placestarend);
        }

        //关闭数据库
        recordsDb.close();
        return recordsList;
    }

    //清除
    public void deleteAllRecords() {
        recordsDb = searchListSQLiteOpenHelper.getWritableDatabase();
        recordsDb.execSQL("delete from "+tableName);

        recordsDb.close();
    }


}
