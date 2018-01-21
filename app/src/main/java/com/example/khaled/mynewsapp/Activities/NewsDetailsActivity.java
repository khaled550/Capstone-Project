package com.example.khaled.mynewsapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.mynewsapp.Data.SqlUtils;
import com.example.khaled.mynewsapp.R;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView articleImg;
    TextView articleTitle;
    TextView articleTxt;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        articleImg = findViewById(R.id.detailImg);
        articleTitle = findViewById(R.id.articleTitleTxt);
        articleTxt = findViewById(R.id.articleTxt);

        if (getIntent() != null){
            currentPos = getIntent().getIntExtra("current_position", 0);
            articleTitle.setText(MainActivity.pieceOfNewsList.get(currentPos).getArticleTitle());
            articleTxt.setText(MainActivity.pieceOfNewsList.get(currentPos).getArticleDescription());

            if (!MainActivity.pieceOfNewsList.get(currentPos).getImgUrl().equals("")){
                Picasso.with(this)
                        .load(MainActivity.pieceOfNewsList.get(currentPos).getImgUrl())
                        .into(articleImg);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.saveOffline) {
            if (!SqlUtils.isFav(this, MainActivity.pieceOfNewsList.get(currentPos).getArticleId())){
                SqlUtils.insertSinglePieceOfNewsToDB(this,MainActivity.pieceOfNewsList.get(currentPos));
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.already_saved), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
