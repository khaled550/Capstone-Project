package com.example.khaled.mynewsapp.Activities;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.khaled.mynewsapp.Adapters.NewsListAdapter;
import com.example.khaled.mynewsapp.Adapters.RecyclerTouchListener;
import com.example.khaled.mynewsapp.Data.NewsContract;
import com.example.khaled.mynewsapp.Data.SqlUtils;
import com.example.khaled.mynewsapp.Models.PieceOfNews;
import com.example.khaled.mynewsapp.NewsWidget;
import com.example.khaled.mynewsapp.Parsing.XmlParser;
import com.example.khaled.mynewsapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Parcelable recyclerViewState;
    public static List<PieceOfNews> pieceOfNewsList;
    private ProgressDialog dialog;

    private FirebaseAnalytics mFirebaseAnalytics;
    private LoaderManager loaderManager;
    private CursorLoader cursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        recyclerView = findViewById(R.id.newsListRecycler);

        if (savedInstanceState == null) {
            createpDialog();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                    dialog.dismiss();
                    NewsWidget.refreshNewsWidgets(MainActivity.this);
                }
            }, 500);
        }else {
            getData();
            recyclerViewState = savedInstanceState.getParcelable("recyclerViewState");
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseCrash.log("Activity created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_latest_news) {
            fetchData();
        }else if (id == R.id.action_saved_offline){
            getSavedNews();
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchData(){
        try {
            pieceOfNewsList  = new XmlParser(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            FirebaseCrash.logcat(Log.ERROR, "PieceOfNewsList", "NPE caught");
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
        bindData();
    }

    public void getSavedNews(){
        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }

    private void bindData(){
        if (pieceOfNewsList != null){
            newsListAdapter = new NewsListAdapter(this, pieceOfNewsList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(newsListAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this
                    , recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID,String.valueOf(pieceOfNewsList.get(position).getArticleId()));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(pieceOfNewsList.get(position).getArticleTitle()));
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PieceOfNewsList");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class)
                            .putExtra("current_position",position);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        recyclerViewState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("recyclerViewState", recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState instanceof Bundle) {
            getData();
            recyclerViewState = (savedInstanceState).getParcelable("recyclerViewState");
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLayoutManager != null)
            recyclerViewState = mLayoutManager.onSaveInstanceState();
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public void createpDialog(){
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(getResources().getString(R.string.latest_news));
        dialog.show();
    }

    private void getData(){
        if (isNetworkOnline())
            fetchData();
        else
            getSavedNews();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        cursorLoader = new CursorLoader(this, NewsContract.NewEntry.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor pieceOfNews) {
        if(pieceOfNews!=null){
            pieceOfNewsList = SqlUtils.getNewsListFromCursor(pieceOfNews);
            bindData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
