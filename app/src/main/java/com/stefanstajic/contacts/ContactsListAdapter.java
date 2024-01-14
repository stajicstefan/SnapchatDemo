package com.stefanstajic.contacts;

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
import com.stefanstajic.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder> {

    private List<Contact> items;
    private List<Contact> originalList;

    public ContactsListAdapter() {
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact contact = this.items.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        Picasso.get().load("https://i.pravatar.cc/300?u=" + contact.getId()).into(holder.avatar);
        holder.itemView.setOnClickListener(view -> {
            Intent profileIntent = new Intent(view.getContext(), ProfileActivity.class);
            profileIntent.putExtra("id", contact.getId());
            profileIntent.putExtra("first_name", contact.getFirstName());
            profileIntent.putExtra("last_name", contact.getLastName());
            profileIntent.putExtra("position", holder.getAbsoluteAdapterPosition());
            profileIntent.putExtra("title", String.format("%s %s", contact.getFirstName(), contact.getLastName()));
            view.getContext().startActivity(profileIntent);
        });
    }

    public void setContacts(List<Contact> result) {
        this.items = result;
        this.originalList = new ArrayList<>(result);
        notifyDataSetChanged();
    }

    public void queryContacts(String query) {
        ArrayList<Contact> filteredList = new ArrayList<>();
        for (int i = 0; this.originalList != null && this.originalList.size() > i; i++) {
            Contact contact = this.originalList.get(i);
            if (contact.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    contact.getLastName().toLowerCase().contains(query)) {
                filteredList.add(contact);
            }
        }
        this.items = filteredList;
        notifyDataSetChanged();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        public final TextView firstName;
        public final TextView lastName;
        public final ImageView avatar;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.firstName = itemView.findViewById(R.id.first_name);
            this.lastName = itemView.findViewById(R.id.last_name);
            this.avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
