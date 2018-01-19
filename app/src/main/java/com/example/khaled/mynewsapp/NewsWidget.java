package com.example.khaled.mynewsapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.khaled.mynewsapp.Activities.MainActivity;
import com.example.khaled.mynewsapp.Models.PieceOfNews;

import java.util.List;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidget extends AppWidgetProvider {

    public static final String IDS_KEY ="widgetids";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (MainActivity.pieceOfNewsList != null){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_widget);
            for (int i =0;i<appWidgetIds.length;i++){
                views.removeAllViews(R.id.widgetContainer);
                for (PieceOfNews pieceOfNews : MainActivity.pieceOfNewsList) {
                    RemoteViews View = new RemoteViews(context.getPackageName(), R.layout.widget_news_item);
                    String title = String.format(Locale.US, "%s %s ","* ", pieceOfNews.getArticleTitle());
                    View.setTextViewText(R.id.widgetNewsTitle, title);
                    views.addView(R.id.widgetContainer, View);
                }
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int[] ids={};
        if (intent.hasExtra(IDS_KEY)) {
            ids = intent.getExtras().getIntArray(IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else super.onReceive(context, intent);
    }

    public static void refreshNewsWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context,NewsWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }
}

