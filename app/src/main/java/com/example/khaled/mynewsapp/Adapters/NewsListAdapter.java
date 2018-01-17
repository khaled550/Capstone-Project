package com.example.khaled.mynewsapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khaled.mynewsapp.Models.PieceOfNews;
import com.example.khaled.mynewsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * Created by khaled on 12/28/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    private List<PieceOfNews> pieceOfNewsList;
    private Context context;
    private int currentId;

    public NewsListAdapter(Context context, List<PieceOfNews> pieceOfNewsList) {
        this.pieceOfNewsList = pieceOfNewsList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView articleTitle;
        ImageView articleImage;
        TextView articleDate;

        public ViewHolder(View view) {
            super(view);
            articleTitle = view.findViewById(R.id.newsTitle);
            articleImage = view.findViewById(R.id.articleImage);
            articleDate = view.findViewById(R.id.newsDate);
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        currentId = pieceOfNewsList.get(position).getArticleId();
        PieceOfNews pieceOfNews = pieceOfNewsList.get(position);
        holder.articleTitle.setText(pieceOfNews.getArticleTitle());
        holder.articleDate.setText(pieceOfNews.getArticlePubDate());

        /*if (!pieceOfNews.getImgUrl().equals("")){
            Picasso.with(this.context)
                    .load(pieceOfNews.getImgUrl())
                    .into(holder.articleImage);
        }*/

        //holder.recipeServings.setText(String.format(Locale.US, "%s %d", "Servings:", recipe.getServings()));
    }

    @Override
    public int getItemCount() {
        return pieceOfNewsList.size();
    }

    public int getItemPosition(){
        return currentId;
    }
}
