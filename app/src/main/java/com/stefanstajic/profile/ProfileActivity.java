package com.stefanstajic.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.stefanstajic.R;
import com.stefanstajic.contacts.Contact;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView profileAvatar = findViewById(R.id.profile_avatar);
        TextInputEditText firstName = findViewById(R.id.first_name);
        TextInputEditText lastName = findViewById(R.id.last_name);
        View update = findViewById(R.id.update);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        Contact profile = new Contact();
        profile.setId(getIntent().getStringExtra("id"));
        profile.setFirstName(getIntent().getStringExtra("first_name"));
        profile.setLastName(getIntent().getStringExtra("last_name"));

        Picasso.get().load("https://i.pravatar.cc/300?u=" + profile.getId()).into(profileAvatar);
        firstName.setText(profile.getFirstName());
        lastName.setText(profile.getLastName());

        update.setOnClickListener(view -> {
            profile.setFirstName(firstName.getText().toString());
            profile.setLastName(lastName.getText().toString());
            new UpdateContactAsyncTask(profile).execute();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteContact(getIntent().getStringExtra("id"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteContact(String id) {
        new DeleteContacAsyncTask(id).execute();
    }

    class DeleteContacAsyncTask extends NetworkAsyncTask<Void, Integer, String> {
        private final String id;

        public DeleteContacAsyncTask(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://dashboard-admin-b00e5.firebaseio.com/contacts/" + id + ".json";
            return deleteRequest(url);
        }

        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }

    class UpdateContactAsyncTask extends NetworkAsyncTask<Void, Integer, String> {
        private final Contact profile;

        public UpdateContactAsyncTask(Contact profile) {
            this.profile = profile;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://dashboard-admin-b00e5.firebaseio.com/contacts/" + profile.getId() + ".json";
            JSONObject data = new JSONObject();
            try {
                data.put("firstName", profile.getFirstName());
                data.put("lastName", profile.getLastName());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return putRequest(url, data);
        }

        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }
}