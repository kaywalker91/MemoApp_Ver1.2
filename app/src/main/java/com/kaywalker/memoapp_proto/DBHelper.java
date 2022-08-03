package com.kaywalker.memoapp_proto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "kaywalker.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TodoList(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Todoitem> getTodoList() {
        ArrayList<Todoitem> todoitems = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()){

                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                Todoitem todoitem = new Todoitem();
                todoitem.setId(id);
                todoitem.setTitle(title);
                todoitem.setContent(content);
                todoitem.setWriteDate(writeDate);

                todoitems.add(todoitem);
            }
        }
        cursor.close();

        return todoitems;
    }

    public void InsertTodo(String _title, String _content, String _writeDate){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO TodoList(title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "')");
    }

    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforedate){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE TodoList SET title = '" + _title + "', content = '" + _content + "', writeDate = '" + _writeDate + "' WHERE writeDate = '" + _beforedate + "'");
    }

    public void DeleteTodo(String _beforedate){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _beforedate + "'");
    }
}
