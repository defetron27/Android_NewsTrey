package com.max.def.newstrey.ViewHolder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.max.def.newstrey.R;

public class NewsQueryViewHolder extends RecyclerView.ViewHolder
{
    public DiagonalLayout diagonalLayout;
    public KenBurnsView kenBurnsImageView;
    public AppCompatTextView author, title, description;
    public RelativeTimeTextView time;

    public View expandView;

    public NewsQueryViewHolder(View itemView)
    {
        super(itemView);

        diagonalLayout = itemView.findViewById(R.id.diagonal_layout);
        kenBurnsImageView = itemView.findViewById(R.id.ken_burns_image_view);
        author = itemView.findViewById(R.id.news_author);
        title = itemView.findViewById(R.id.news_title);
        description = itemView.findViewById(R.id.news_description);
        time = itemView.findViewById(R.id.news_time);
        expandView = itemView.findViewById(R.id.expand_view);
    }
}
