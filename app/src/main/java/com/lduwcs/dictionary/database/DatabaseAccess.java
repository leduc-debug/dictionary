package com.lduwcs.dictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private static String TABLE_NAME;

    private DatabaseAccess(Context context,String tableName) {
        this.openHelper = new DatabaseOpenHelper(context,0);
        this.TABLE_NAME=tableName;
    }

    public static DatabaseAccess getInstance(Context context,String tableName) {
        if (instance == null) {
            instance = new DatabaseAccess(context,tableName);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getWords() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        cursor.moveToFirst();
        int i=0;
        while (!cursor.isAfterLast()) {
            i+=1;
            if(i>=15) break;
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public ArrayList<String> getWords(String filter) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" where word like '"+ filter +"%' limit 10", null);
        cursor.moveToFirst();
        int i=0;
        while (!cursor.isAfterLast()) {
            i+=1;
            if(i>=15) break;
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public String getDefinition(String word) {
        String definition = "";
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" where word='"+ word +"'", null);
        cursor.moveToFirst();

        definition  = cursor.getString(2);

        cursor.close();
        return definition;
    }

}
