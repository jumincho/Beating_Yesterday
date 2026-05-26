package com.jumincho.beatingyesterday.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDatabase {
    private static final String TAG = "NoteDatabase";

    private static NoteDatabase database;
    public static String DATABASE_NAME = "todo.db";
    public static String TABLE_NOTE = "NOTE";
    public static int DATABASE_VERSION = 1;

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    private NoteDatabase(Context context) {
        this.context = context;
    }

    public static NoteDatabase getInstance(Context context) {
        if (database == null) {
            database = new NoteDatabase(context);
        }
        return database;
    }

    public Cursor rawQuery(String sql) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in rawQuery", ex);
        }
        return cursor;
    }

    public boolean execSQL(String sql) {
        try {
            Log.d(TAG, "SQL : " + sql);
            db.execSQL(sql);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in execSQL", ex);
            return false;
        }
        return true;
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return db.insert(table, nullColumnHack, values);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in insert", ex);
            return -1;
        }
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        try {
            return db.delete(table, whereClause, whereArgs);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in delete", ex);
            return 0;
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, selectionArgs);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in rawQuery", ex);
        }
        return cursor;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String dropSql = "drop table if exists " + TABLE_NOTE;
            try {
                db.execSQL(dropSql);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in dropSql", ex);
            }

            String createSql = "create table " + TABLE_NOTE + "("
                    + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TODO TEXT DEFAULT '' "
                    + ")";
            try {
                db.execSQL(createSql);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in createSql", ex);
            }

            String createIndexSql = "create index " + TABLE_NOTE + "_IDX ON " + TABLE_NOTE + "("
                    + "_id"
                    + ")";
            try {
                db.execSQL(createIndexSql);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in createIndexSql", ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public boolean open() {
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    public void close() {
        db.close();
        database = null;
    }
}
