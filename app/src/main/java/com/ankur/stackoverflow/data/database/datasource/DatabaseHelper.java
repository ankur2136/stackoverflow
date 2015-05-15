package com.ankur.stackoverflow.data.database.datasource;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        // executeQueries(TableName.getCreateTableQueries(), db);

        // Create Indexes
        // executeQueries(TableName.getCreateIndexQueries(), db);

        // Create triggers
    }

    @Override
    // TODO Sync unsynced items before dropping table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // executeQueries(TableName.getDropTableQueries(), db);
        onCreate(db);
    }

    private void executeQueries(List<String> queries, SQLiteDatabase db) {
        for (String query : queries) {
            db.execSQL(query);
        }
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
