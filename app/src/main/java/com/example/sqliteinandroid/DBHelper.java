package com.example.sqliteinandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String ID = "stdid";
    public static final String NAME = "stdname";
    public static final String RID = "rid";
    public static final String DATABASE_NAME = "mydb";
    public static final String DATABASE_TABLE = "student";
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //String DB_CREATE = "CREATE DATABASE " + DATABASE_NAME;
        String TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" + RID + "PRIMARYKEY, AUTO_INCREMENT, " + ID + ", TEXT, " + NAME + " text)";
        //sqLiteDatabase.execSQL(DB_CREATE);
        sqLiteDatabase.execSQL(TABLE_CREATE);
        Log.d("Table", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
    }
}
