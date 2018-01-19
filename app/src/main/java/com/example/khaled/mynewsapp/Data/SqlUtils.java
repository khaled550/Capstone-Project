package com.example.khaled.mynewsapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.khaled.mynewsapp.Models.PieceOfNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khaled on 1/17/18.
 */

public class SqlUtils {

    private static String TAG = SqlUtils.class.getSimpleName();

    public static List<PieceOfNews> getNewsListFromCursor(Cursor cursor) {

        List<PieceOfNews> pieceOfNewsList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PieceOfNews pieceOfNews = new PieceOfNews();

            pieceOfNews.setArticleId(cursor.getInt(cursor.getColumnIndex(NewsContract.NewEntry.News_ID)));
            pieceOfNews.setArticleTitle(cursor.getString(cursor.getColumnIndex(NewsContract.NewEntry.News_Title)));
            pieceOfNews.setImgUrl(cursor.getString(cursor.getColumnIndex(NewsContract.NewEntry.News_Img)));
            pieceOfNews.setArticleLink(cursor.getString(cursor.getColumnIndex(NewsContract.NewEntry.News_Link)));
            pieceOfNews.setArticleDescription(cursor.getString(cursor.getColumnIndex(NewsContract.NewEntry.News_Description)));
            pieceOfNews.setArticleCategory(cursor.getString(cursor.getColumnIndex(NewsContract.NewEntry.News_Category)));
            pieceOfNews.setArticlePubDate(cursor.getColumnName(cursor.getColumnIndex(NewsContract.NewEntry.publish_time)));
            pieceOfNewsList.add(pieceOfNews);
            cursor.moveToNext();
        }
        return pieceOfNewsList;
    }

    public static void insertSinglePieceOfNewsToDB(Context context, PieceOfNews pieceOfNews){

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewEntry.News_ID, pieceOfNews.getArticleId());
        contentValues.put(NewsContract.NewEntry.News_Title, pieceOfNews.getArticleTitle());
        contentValues.put(NewsContract.NewEntry.publish_time, pieceOfNews.getArticlePubDate());
        contentValues.put(NewsContract.NewEntry.News_Description, pieceOfNews.getArticleDescription());
        contentValues.put(NewsContract.NewEntry.News_Link, pieceOfNews.getArticleLink());
        contentValues.put(NewsContract.NewEntry.News_Img, pieceOfNews.getImgUrl());
        contentValues.put(NewsContract.NewEntry.News_Category, pieceOfNews.getArticleCategory());

        Uri uri = context.getContentResolver().insert(NewsContract.NewEntry.CONTENT_URI, contentValues);

        if (uri != null){
            Log.e(TAG, "Success insert data uri: "+uri.toString());
        }else
            Log.e(TAG, "Failed insert data uri: "+pieceOfNews.getArticleTitle());
    }

    public static Uri getpieceOfNewsUriByID(int id) {
        Uri uri = Uri.parse(NewsContract.NewsDataEntity.BASE_CONTENT_URI.toString() + "/"
                + NewsContract.NewsDataEntity.NEWS_PATH + "/" + id);

        return uri;
    }

    public static boolean isFav(Context context, int id) {
        Cursor detailsCursor = context.getContentResolver().query(getpieceOfNewsUriByID(id), null, null, null, null);

        if (detailsCursor != null) {
            if (detailsCursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }
}
