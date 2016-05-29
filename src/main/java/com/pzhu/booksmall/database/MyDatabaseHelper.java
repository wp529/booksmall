package com.pzhu.booksmall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pzhu.booksmall.Utils.UIUtils;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_BOOK = "create table Book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)";
    private static final String CREATE_SHOPPING = "create table Shopping (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
        sqLiteDatabase.execSQL(CREATE_SHOPPING);
        ContentValues values = new ContentValues();
        for(int i=0;i<21;i++){
            values.put("name","软件工程" + i);
            values.put("author","齐治昌" + i);
            values.put("price",50 + i * 1.21);
            values.put("pages", 300 + i*10);
            sqLiteDatabase.insert("Book",null,values);
            values.clear();
        }
        UIUtils.makeToast(mContext, "create success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        UIUtils.makeToast(mContext, "update success");
    }
}
