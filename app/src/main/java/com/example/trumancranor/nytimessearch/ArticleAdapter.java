package com.example.trumancranor.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.trumancranor.nytimessearch.R.id.ivImage;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id. ivImage) ImageView ivImage;

        public Article article;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        final ViewHolder viewHolder = new ViewHolder(articleView);
        articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ArticleActivity.class);
                i.putExtra("article", Parcels.wrap(viewHolder.article));
                getContext().startActivity(i);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        holder.article = articles.get(position);

        holder.tvTitle.setText(holder.article.getHeadline());

        holder.ivImage.setImageResource(0);
        String thumbnail = holder.article.getThumbnailUrl();
        if (!thumbnail.isEmpty()) {
            Picasso.with(getContext()).load(thumbnail).into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
