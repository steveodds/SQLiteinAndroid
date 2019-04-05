package com.example.sqliteinandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {
    /*public static final String ID = "stdid";
    public static final String NAME = "stdname";
    public static final String RID = "rid";*/
    public static final String DATABASE_NAME = "std";
//    public static final String DATABASE_TABLE = "student";
    public static final int DATABASE_VERSION = 1;
    private static final String TABLE_M = "mytable";
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String DB_CREATE = "CREATE DATABASE " + DATABASE_NAME;
        String TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" + RID + "PRIMARYKEY, AUTO_INCREMENT, " + ID + ", TEXT, " + NAME + " text)";
        sqLiteDatabase.execSQL(DB_CREATE);
        sqLiteDatabase.execSQL(TABLE_CREATE);
        Log.d("Table", "Table created");*/

        String CREATE = "CREATE TABLE " + TABLE_M +"(regno TEXT,name TEXT)"; db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_M); onCreate(db);
    }

    public void writeData(String regno,String name){
        db=getWritableDatabase();
        db.execSQL("insert into mytable values ('"+regno+"','"+name +"')");
    }

    public void updateData(String regno,String name){
        db=getWritableDatabase();
        db.execSQL("update mytable set name='"+name +"'where regno='"+regno+"'");
    }

    public void deleteData(String regno){
        db=getWritableDatabase();
        db.execSQL("delete from mytable where regno='"+regno+"'");
    }

    public Cursor readData(String regno) {
        Cursor c1 = null;
        try {
            db = this.getReadableDatabase();
            c1 = db.rawQuery("select name from mytable where regno="+regno, null);
        }
        catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
        return c1;
    }
}
