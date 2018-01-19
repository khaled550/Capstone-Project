package com.example.khaled.mynewsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Parcelable recyclerViewState;
    public static List<PieceOfNews> pieceOfNewsList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            createpDialog();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isNetworkOnline())
                        fetchData();
                    else
                        getSavedNews();
                    dialog.dismiss();
                    NewsWidget.refreshNewsWidgets(MainActivity.this);
                }
            }, 2500);
        }
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
            e.printStackTrace();
        }
        bindData();
    }

    public void getSavedNews(){
        pieceOfNewsList = SqlUtils.getNewsListFromCursor(this.getContentResolver().query(NewsContract.NewEntry.CONTENT_URI,
                null,
                null,
                null,
                NewsContract.NewEntry.News_ID));

        bindData();
    }

    private void bindData(){
        if (pieceOfNewsList != null){
            recyclerView = findViewById(R.id.newsListRecycler);

            newsListAdapter = new NewsListAdapter(this, pieceOfNewsList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(newsListAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this
                    , recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class)
                            .putExtra("current_position",position);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            dialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (recyclerView != null){
            recyclerViewState = mLayoutManager.onSaveInstanceState();
            outState.putParcelable("recyclerViewState", recyclerView.getLayoutManager().onSaveInstanceState());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
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
        dialog.setMessage("Loading Latest News...");
        dialog.show();
    }
}
