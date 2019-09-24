package com.tanjun.toutiao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tanjun.commonlib.MyApplication;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "toutiao";
    private static final int DB_VERSION = 6;
    private static final String CLEAR_TABLE_DATA = "delete_from";
    private static final String DROP_TABLE = "drop table if exist";
    private static DatabaseHelper instance = null;
    private static SQLiteDatabase db = null;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static synchronized DatabaseHelper getInstance(){
        if (instance == null) {
            instance = new DatabaseHelper(MyApplication.appContext, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    public static synchronized SQLiteDatabase getDataBase(){
        if (db == null) {
            db = getInstance().getWritableDatabase();
        }
        return db;
    }

    public static synchronized void closeDataBase(){
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
