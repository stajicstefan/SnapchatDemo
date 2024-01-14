package com.stefanstajic.conversations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stefanstajic.R;
import com.stefanstajic.contacts.Contact;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConversationsFragment extends Fragment {
    private RecyclerView conversationsList;
    private ConverstationsListAdapter adapter = new ConverstationsListAdapter();

    public static ConversationsFragment newInstance() {

        Bundle args = new Bundle();

        ConversationsFragment fragment = new ConversationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetContactsAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        Context context = view.getContext();
        conversationsList = view.findViewById(R.id.list);
        conversationsList.setLayoutManager(new LinearLayoutManager(context));
        conversationsList.setNestedScrollingEnabled(false);
        conversationsList.setAdapter(adapter);
        conversationsList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        return view;
    }

    class GetContactsAsyncTask extends NetworkAsyncTask<Void, Integer, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            String stringUrl = "https://dashboard-admin-b00e5.firebaseio.com/contacts.json";
            String result = getRequest(stringUrl);
            return convertToContacts(result);
        }

        private List<Contact> convertToContacts(String result) {
            System.out.println(result);
            try {
                JSONObject data = new JSONObject(result);
                Iterator<String> iterator = data.keys();
                List<Contact> contacts = new ArrayList<>();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    System.out.println(key);
                    JSONObject object = data.getJSONObject(key);
                    System.out.println(object);
                    Contact contact = new Contact();
                    contact.setFirstName(object.getString("firstName"));
                    contact.setLastName(object.getString("lastName"));
                    contact.setId(key);
                    contacts.add(contact);
                }
                return contacts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Contact> result) {
            adapter.setContacts(result);
        }
    }

}
