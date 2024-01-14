package com.stefanstajic.conversations;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stefanstajic.R;
import com.stefanstajic.contacts.Contact;

import java.util.ArrayList;
import java.util.List;

public class ConverstationsListAdapter extends RecyclerView.Adapter<ConverstationsListAdapter.ConversationViewHolder> {
    private List<Contact> items = new ArrayList<>();

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Contact contact = this.items.get(position);
        holder.content.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
        Picasso.get().load("https://i.pravatar.cc/300?u=" + contact.getId()).into(holder.avatar);
        View.OnClickListener openChatListener = view -> {
            Intent chatIntent = new Intent(view.getContext(), ChatActivity.class);
            chatIntent.putExtra("name", holder.content.getText().toString());
            chatIntent.putExtra("id", contact.getId());
            view.getContext().startActivity(chatIntent);
        };
        holder.itemView.setOnClickListener(openChatListener);
        holder.chatOpen.setOnClickListener(openChatListener);
    }

    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    public void setContacts(List<Contact> result) {
        this.items = result;
        notifyDataSetChanged();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        public final TextView content;
        public final ImageView avatar;
        public final View chatOpen;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.content);
            this.avatar = itemView.findViewById(R.id.avatar);
            this.chatOpen = itemView.findViewById(R.id.chat_open);
        }
    }
}
