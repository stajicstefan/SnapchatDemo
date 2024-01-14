package com.stefanstajic.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;
import com.stefanstajic.R;
import com.stefanstajic.contacts.Contact;
import com.stefanstajic.contacts.ContactsListAdapter;
import com.stefanstajic.contacts.CreateContactActivity;
import com.stefanstajic.conversations.ConversationsFragment;
import com.stefanstajic.feeds.FeedFragment;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView contactsList;
    private ContactsListAdapter contactsAdapter = new ContactsListAdapter();
    private SearchView searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView profileAvatar = findViewById(R.id.profile_avatar);
        ViewPager2 viewPager = findViewById(R.id.content_pager);
        searchBox = findViewById(R.id.search_box);
        contactsList = findViewById(R.id.contact_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsAdapter.queryContacts(newText);
                return false;
            }
        });

        viewPager.setAdapter(new ContentPager(this));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    contactsList.setVisibility(View.VISIBLE);
                    searchBox.setVisibility(View.VISIBLE);
                } else {
                    contactsList.setVisibility(View.GONE);
                    searchBox.setVisibility(View.GONE);
                }
            }
        });

        contactsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        contactsList.setAdapter(contactsAdapter);

        Picasso.get().load("https://i.pravatar.cc/300?img=22").into(profileAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo_testing, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onResume() {
        super.onResume();
        this.searchBox.setQuery("", false);
        this.searchBox.setIconified(true);
        new GetContactsAsyncTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_contact) {
            launchCreateContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCreateContact() {
        Intent createContactIntent = new Intent(this, CreateContactActivity.class);
        startActivity(createContactIntent);
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
            if (result != null) {
                contactsAdapter.setContacts(result);
            }
        }
    }

    static class ContentPager extends FragmentStateAdapter {

        public ContentPager(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return FeedFragment.newInstance();
            }
            if (position == 1) {
                return ConversationsFragment.newInstance();
            }
            return FeedFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}