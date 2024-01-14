package com.stefanstajic.conversations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stefanstajic.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageViewHolder> {
    private List<Message> items;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public MessagesListAdapter(List<Message> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = this.items.get(position);
        holder.text.setText(message.getText());
        holder.time.setText(timeFormat.format(new Date(message.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        public final TextView text;
        public final TextView time;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);
            this.time = itemView.findViewById(R.id.time);
        }
    }
}
