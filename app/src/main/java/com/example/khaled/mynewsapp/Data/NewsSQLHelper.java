package com.example.khaled.mynewsapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by khaled on 1/17/18.
 */

public class NewsSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NewsDataBase.db";
    private static final int VERSION = 1;

    public NewsSQLHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    public NewsSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES = "CREATE TABLE " +
                NewsContract.NewEntry.TABLE_NAME + "(" +
                NewsContract.NewEntry.News_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsContract.NewEntry.News_Title + " TEXT NOT NULL, " +
                NewsContract.NewEntry.News_Link + " TEXT NOT NULL, " +
                NewsContract.NewEntry.News_Description + " TEXT NOT NULL, " +
                NewsContract.NewEntry.News_Img + " TEXT NOT NULL, " +
                NewsContract.NewEntry.News_Category + " TEXT NOT NULL, " +
                NewsContract.NewEntry.publish_time + " TEXT NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 2)
            sqLiteDatabase.execSQL("ALTER TABLE "+ NewsContract.NewEntry.TABLE_NAME+" ADD COLUMN "
                    + NewsContract.NewEntry.News_Title+" TEXT ");

        onCreate(sqLiteDatabase);
    }
}
