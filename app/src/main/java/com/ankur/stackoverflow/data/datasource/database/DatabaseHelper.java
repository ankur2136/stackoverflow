package com.ankur.stackoverflow.data.datasource.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int       VERSION       = 1;

    public static final String    DATABASE_NAME = "com.ankur.stackoverflow.data.database";

    private static DatabaseHelper helper        = null;

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        executeQueries(ItemDataSource.getCreateTableQueries(), db);

        // Create Indexes
        executeQueries(ItemDataSource.getCreateIndexQueries(), db);
    }

    private void executeQueries(List<String> queries, SQLiteDatabase db) {
        for (String query : queries) {
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
