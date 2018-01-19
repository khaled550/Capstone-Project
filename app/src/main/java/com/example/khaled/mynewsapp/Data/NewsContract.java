package com.example.khaled.mynewsapp.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.khaled.mynewsapp.Data.NewsContract.NewsDataEntity.BASE_CONTENT_URI;
import static com.example.khaled.mynewsapp.Data.NewsContract.NewsDataEntity.NEWS_PATH;

/**
 * Created by khaled on 1/17/18.
 */

public class NewsContract {

    public static class NewsDataEntity implements BaseColumns {

        public static final String AUTHORITY = "com.example.khaled.mynewsapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        public static final String NEWS_PATH = "News";
    }

    public static class NewEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(NEWS_PATH).build();

        public static final String TABLE_NAME = "News";

        public final static String News_ID = "news_id";
        final static String News_Title = "newsTitle";
        final static String News_Link = "newsLink";
        final static String News_Img = "newsImage";
        final static String News_Description = "newsDesc";
        final static String News_Category = "newsCategory";
        final static String publish_time = "newsPublishDate";

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }
}

