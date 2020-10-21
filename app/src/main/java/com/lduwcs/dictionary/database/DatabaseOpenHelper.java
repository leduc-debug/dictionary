package com.lduwcs.dictionary.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String[] DATABASE_NAME = new String[]{"anh_viet.db","viet_anh.db"};
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context,int type) {
        super(context, DATABASE_NAME[type], null, DATABASE_VERSION);

    }
}
