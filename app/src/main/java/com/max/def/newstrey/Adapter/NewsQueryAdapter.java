package com.max.def.newstrey.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.max.def.newstrey.Models.NewsQueryArticlesModel;
import com.max.def.newstrey.NewsDetailsActivity;
import com.max.def.newstrey.R;
import com.max.def.newstrey.Utils.ISO8601Parse;
import com.max.def.newstrey.ViewHolder.NewsQueryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class NewsQueryAdapter extends RecyclerView.Adapter<NewsQueryViewHolder>
{
    private Activity activity;
    private List<NewsQueryArticlesModel> articlesModel;

    public NewsQueryAdapter(Activity activity, List<NewsQueryArticlesModel> articlesModel)
    {
        this.activity = activity;
        this.articlesModel = articlesModel;
    }

    @NonNull
    @Override
    public NewsQueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.news_source_layout,parent,false);
        return new NewsQueryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final NewsQueryViewHolder holder, int position)
    {
        final NewsQueryArticlesModel newsQueryArticlesModel = articlesModel.get(position);

        Date date = null;

        try
        {
            date = ISO8601Parse.parse(newsQueryArticlesModel.getPublishedAt());
        }
        catch(Exception e)
        {
            Log.e("NewsSourceAdapter",e.getLocalizedMessage());
        }

        String newsAuthor =  newsQueryArticlesModel.getAuthor();
        String newsTitle = newsQueryArticlesModel.getTitle();
        String newsArticlesDescription = newsQueryArticlesModel.getDescription();
        String newsUrlToImage = newsQueryArticlesModel.getUrlToImage();

        if (newsAuthor !=  null && !newsAuthor.equals("null"))
        {
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(newsAuthor);
        }
        else
        {
            holder.author.setVisibility(View.GONE);
            holder.author.setText("");
        }

        if (newsTitle !=  null && !newsTitle.equals("null"))
        {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(newsTitle);
        }
        else
        {
            holder.title.setVisibility(View.GONE);
            holder.title.setText("");
        }

        if(newsUrlToImage == null || newsUrlToImage.isEmpty())
        {
            holder.diagonalLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.diagonalLayout.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(newsUrlToImage).into(holder.kenBurnsImageView);
        }

        if (date != null)
        {
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setReferenceTime(date.getTime());
        }
        else
        {
            holder.time.setVisibility(View.GONE);
        }

        if (newsArticlesDescription !=  null && !newsArticlesDescription.equals("null"))
        {
            holder.description.setVisibility(View.VISIBLE);

            if (newsArticlesDescription.length() > 100)
            {
                String shortDescription = newsArticlesDescription.substring(0,100) + "...";

                holder.description.setText(shortDescription);
                holder.expandView.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.description.setText(newsTitle);
                holder.expandView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.description.setVisibility(View.GONE);
            holder.description.setText("");
            holder.expandView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String newsArticleUrl = newsQueryArticlesModel.getUrl();

                Intent intent = new Intent(activity, NewsDetailsActivity.class);
                intent.putExtra("url", newsArticleUrl);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return articlesModel.size();
    }
}