package com.lduwcs.dictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.lduwcs.dictionary.recycleview.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private static String TABLE_NAME;
    private Context mContext;

    private DatabaseAccess(Context context,String tableName) {
        this.openHelper = new DatabaseOpenHelper(context,0);
        this.TABLE_NAME=tableName;
        this.mContext = context;
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
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" limit 10", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
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

    public boolean addFavorite(String word){
//        Cursor cursor = database.rawQuery("SELECT id FROM "+TABLE_NAME+" where word='"+ word +"'", null);
        Cursor cursor = database.rawQuery("SELECT id FROM "+TABLE_NAME+ " where word='"+ word +"'", null);
        cursor.moveToFirst();
        String id = cursor.getString(0);
        cursor.close();
        try{
            database.execSQL("INSERT INTO favorite(id) VALUES ( "+id+" )");
            Toast.makeText(mContext,"Add favorite word success",Toast.LENGTH_LONG).show();
            return true;
        }
        catch (Exception e){
            Toast.makeText(mContext,"Add favorite word fail",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean clearFavorite(String id){
        try{
            database.execSQL("DELETE FROM favorite WHERE id='"+id+"'");
            Toast.makeText(mContext,"Clear favorite word success",Toast.LENGTH_LONG).show();
            return true;
        }
        catch (Exception e){
            Toast.makeText(mContext,"Clear favorite word fail" + e.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public String getFavoriteId(String word){
        Cursor cursor = database.rawQuery("SELECT id FROM "+TABLE_NAME+ " where word='"+ word +"'", null);
        cursor.moveToFirst();
        String id = cursor.getString(0);
        Cursor cursor2 = database.rawQuery("SELECT id FROM favorite"+ " where id='"+ id +"'", null);
        cursor2.moveToFirst();
        try{
            String ss = cursor2.getString(0);
            return id;
        }catch (Exception e){
            return "0";
        }
    }

    public ArrayList<String> getFavoriteIds() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM favorite", null);
        cursor.moveToFirst();
        int i=0;
        while (!cursor.isAfterLast()) {
            i+=1;
            if(i>=20) break;
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Word getWord(String id){
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet Where id= '"+id+"'", null);
        cursor.moveToFirst();
        return new Word(cursor.getString(1),"Tự nhớ đi");
    }
}
