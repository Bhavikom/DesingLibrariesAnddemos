package com.yasoka.eazyscreenrecord.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String[] ALL_FAVORITE_COLUMNS = {FAVORITE_TITLE, FAVORITE_USERNAME, FAVORITE_DURATION, FAVORITE_UPLOAD_DATE, FAVORITE_VIDEO_ID};
    private static final String DBName = "scr_db";
    private static final int DBVersion = 1;
    private static final String FAVORITE_CREATE_TABLE = "create table if not exists tbl_favorite(col_favorite_video_id TEXT, col_favorite_title TEXT, col_favorite_username TEXT, col_favorite_duration TEXT, col_favorite_upload_date TEXT);";
    public static final String FAVORITE_DURATION = "col_favorite_duration";
    public static final String FAVORITE_TITLE = "col_favorite_title";
    public static final String FAVORITE_UPLOAD_DATE = "col_favorite_upload_date";
    public static final String FAVORITE_USERNAME = "col_favorite_username";
    public static final String FAVORITE_VIDEO_ID = "col_favorite_video_id";
    public static final String ROW_ID = "rowid";
    public static final String TABLE_FAVORITE = "tbl_favorite";

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DBHelper(Context context) {
        super(context, DBName, null, 1);
    }

    public DBHelper(@Nullable Context context, @Nullable String str, @Nullable CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(FAVORITE_CREATE_TABLE);
    }
}
