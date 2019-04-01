package com.squaredcandy.androidtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TvMazeApiAdapter extends RecyclerView.Adapter<TvMazeApiAdapter.ViewHolder> {

    boolean showLong = false;
    private ArrayList<TvMazeApiModel> models;
    private View.OnClickListener clickListener;

    public TvMazeApiAdapter(ArrayList<TvMazeApiModel> models, boolean showLong) {
        this.models = models;
        this.showLong = showLong;
    }

    public void setClickListener(View.OnClickListener callback) {
        clickListener = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row, parent,false);

        ViewHolder holder = new ViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TvMazeApiModel model = models.get(i);
        viewHolder.title.setText(model.getName());
        if(showLong) {
            String textStr = "Season: " + model.getSeason() + "\nEpisode: " + model.getEpisode() +
                    "\nAired on: " + model.getAirStamp() + "\nRun Time: " +
                    model.getRunTime() + " mins\nSummary:\n" + model.getSummary() + "\n\nPreview:\n";
            viewHolder.text.setText(textStr);
            Glide.with(viewHolder.image.getContext())
                    .load(model.getOriginalImage()).into(viewHolder.image);
        }
        else {
            viewHolder.text.setText("Summary:\n" + model.getSummary() + "\n\nPreview:\n");
            Glide.with(viewHolder.image.getContext())
                    .load(model.getMediumImage()).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, text;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            text = view.findViewById(R.id.text);
            image = view.findViewById(R.id.image);
        }
    }
}
