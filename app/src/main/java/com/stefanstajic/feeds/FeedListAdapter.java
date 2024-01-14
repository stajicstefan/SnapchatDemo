package com.stefanstajic.feeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stefanstajic.R;

import java.util.List;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedViewHolder> {
    private List<Feed> items;

    public FeedListAdapter(List<Feed> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed feed = this.items.get(position);
        Picasso.get().load(feed.getImageUrl()).into(holder.background);
        holder.caption.setText(feed.getCaption());
    }

    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    public void setFeed(List<Feed> result) {
        this.items = result;
        notifyDataSetChanged();
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        public final ImageView background;
        public final TextView caption;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            this.background = itemView.findViewById(R.id.background);
            this.caption = itemView.findViewById(R.id.caption);
        }
    }
}
