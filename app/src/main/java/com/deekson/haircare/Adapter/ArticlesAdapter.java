package com.deekson.haircare.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deekson.haircare.Model.Article;
import com.deekson.haircare.R;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    public ArrayList<Article> mArticle ;
    public Context context;
    public Activity activity;

    public ArticlesAdapter(Context context,ArrayList<Article> articles){
        this.mArticle =articles;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article,parent,false);
        ViewHolder viewHolder =new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindArticles(mArticle.get(position));
    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle,articleContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleContent= itemView.findViewById(R.id.articleContent);
            articleTitle =itemView.findViewById(R.id.articleTitle);
        }

        public void bindArticles(Article article) {
            articleContent.setText(article.getArticle());
            articleTitle.setText(article.getTitle());
        }
    }
}
