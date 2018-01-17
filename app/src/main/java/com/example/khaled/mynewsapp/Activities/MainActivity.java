package com.example.khaled.mynewsapp.Activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.khaled.mynewsapp.Adapters.NewsListAdapter;
import com.example.khaled.mynewsapp.Adapters.RecyclerTouchListener;
import com.example.khaled.mynewsapp.Models.PieceOfNews;
import com.example.khaled.mynewsapp.Parsing.XmlParser;
import com.example.khaled.mynewsapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Parcelable recyclerState;
    public static List<PieceOfNews> pieceOfNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*PieceOfNews pieceOfNews = new  PieceOfNews(1,2, "Title 1","", "",
        "","", "","1/1/2018");
        PieceOfNews pieceOfNews1 = new  PieceOfNews(1,2, "Title 2","", "",
                "","", "","2/1/2018");
        pieceOfNewsList = new ArrayList<>();
        pieceOfNewsList.add(pieceOfNews);
        pieceOfNewsList.add(pieceOfNews1);*/

        try {
            pieceOfNewsList  = new XmlParser(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.newsListRecycler);

        newsListAdapter = new NewsListAdapter(this, pieceOfNewsList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(newsListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this
                , recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Intent intent = new Intent(this, RecipeInfoActivity.class)
                        .putExtra("current_id",recipeList.get(position).getId());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
