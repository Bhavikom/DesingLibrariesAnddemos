package com.yasoka.eazyscreenrecord.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideosData;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private Cursor cursor;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DataSource(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public boolean addToFavorite(VideosData videosData) {
        if (isFavoriteExist(videosData.getVideoId())) {
            Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), "Already exist in favorite.", 0).show();
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FAVORITE_TITLE, videosData.getTitle());
        contentValues.put(DBHelper.FAVORITE_USERNAME, videosData.getUserName());
        contentValues.put(DBHelper.FAVORITE_DURATION, videosData.getDuration());
        contentValues.put(DBHelper.FAVORITE_UPLOAD_DATE, videosData.getMobileDtAdded());
        contentValues.put(DBHelper.FAVORITE_VIDEO_ID, videosData.getVideoId());
        if (this.database.insert(DBHelper.TABLE_FAVORITE, null, contentValues) != -1) {
            Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), "Added in favorite.", 0).show();
            return true;
        }
        Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), "Error while adding in favorite.", 0).show();
        return false;
    }

    public List<VideosData> getAllFavorites() {
        ArrayList arrayList = new ArrayList();
        this.cursor = this.database.query(DBHelper.TABLE_FAVORITE, DBHelper.ALL_FAVORITE_COLUMNS, null, null, null, null, "col_favorite_upload_date DESC", null);
        if (this.cursor.getCount() > 0) {
            this.cursor.moveToFirst();
            while (!this.cursor.isAfterLast()) {
                arrayList.add(cursorToFavorite(this.cursor));
                this.cursor.moveToNext();
            }
        }
        return arrayList;
    }

    private VideosData cursorToFavorite(Cursor cursor2) {
        VideosData videosData = new VideosData();
        videosData.setTitle(cursor2.getString(cursor2.getColumnIndex(DBHelper.FAVORITE_TITLE)));
        videosData.setUserName(cursor2.getString(cursor2.getColumnIndex(DBHelper.FAVORITE_USERNAME)));
        videosData.setMobileDtAdded(cursor2.getString(cursor2.getColumnIndex(DBHelper.FAVORITE_UPLOAD_DATE)));
        videosData.setVideoId(cursor2.getString(cursor2.getColumnIndex(DBHelper.FAVORITE_VIDEO_ID)));
        videosData.setDuration(cursor2.getString(cursor2.getColumnIndex(DBHelper.FAVORITE_DURATION)));
        return videosData;
    }

    public boolean deleteFavorite(String str) {
        if (this.database.delete(DBHelper.TABLE_FAVORITE, "col_favorite_video_id=?", new String[]{str}) > 0) {
            Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), "Deleted Successfully.", 0).show();
            return true;
        }
        Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), "Error while deleting.", 0).show();
        return false;
    }

    private boolean isFavoriteExist(String str) {
        this.cursor = this.database.query(DBHelper.TABLE_FAVORITE, DBHelper.ALL_FAVORITE_COLUMNS, "col_favorite_video_id=?", new String[]{str}, null, null, null);
        return this.cursor.getCount() > 0;
    }
}
