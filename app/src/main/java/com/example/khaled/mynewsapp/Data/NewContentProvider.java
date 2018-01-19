package com.example.khaled.mynewsapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.khaled.mynewsapp.Data.NewsContract.NewEntry.TABLE_NAME;

/**
 * Created by khaled on 1/17/18.
 */

public class NewContentProvider extends ContentProvider {

    NewsSQLHelper newsDBHelper;
    public static final int NEWS = 100;
    private static final int NEWS_BY_ID = 101;
    private static final UriMatcher staticUriMatcher = BuildUriMatcher();

    @Override
    public boolean onCreate() {

        Context context = getContext();
        newsDBHelper = new NewsSQLHelper(context);

        return false;
    }

    public static UriMatcher BuildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(NewsContract.NewsDataEntity.AUTHORITY
                , NewsContract.NewsDataEntity.NEWS_PATH, NEWS);

        uriMatcher.addURI(NewsContract.NewsDataEntity.AUTHORITY
                , NewsContract.NewsDataEntity.NEWS_PATH + "/#", NEWS_BY_ID);

        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = newsDBHelper.getReadableDatabase();

        int match = staticUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case NEWS:
                cursor = sqLiteDatabase.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case NEWS_BY_ID: {
                long _id = NewsContract.NewEntry.getIdFromUri(uri);
                cursor = sqLiteDatabase.query(
                        TABLE_NAME,
                        projection,
                        NewsContract.NewEntry.News_ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Not Found Uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase sqLiteDatabase = newsDBHelper.getWritableDatabase();

        int match = staticUriMatcher.match(uri);
        Uri returnedUri;

        switch (match) {
            case NEWS:

                long id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnedUri = ContentUris.withAppendedId(NewsContract.NewEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsDeleted;
        int match = staticUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = newsDBHelper.getWritableDatabase();

        switch (match) {
            case NEWS:
                rowsDeleted = sqLiteDatabase.delete(NewsContract.NewEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsUpdated;
        int match = staticUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = newsDBHelper.getWritableDatabase();

        switch (match) {

            case NEWS:
                rowsUpdated = sqLiteDatabase.update(
                        NewsContract.NewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        int matcher = staticUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = newsDBHelper.getWritableDatabase();

        switch (matcher) {
            case NEWS: {
                sqLiteDatabase.beginTransaction();
                int count = 0;

                for (ContentValues item : values) {
                    long _id = sqLiteDatabase.insert(NewsContract.NewEntry.TABLE_NAME, null, item);
                    if (_id != -1) {
                        count++;
                    }
                }
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();

                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

