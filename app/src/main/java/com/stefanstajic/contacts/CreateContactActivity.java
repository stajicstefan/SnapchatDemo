package com.stefanstajic.contacts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stefanstajic.R;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View submitButton = findViewById(R.id.submit);
        TextInputEditText firstName = findViewById(R.id.first_name);
        TextInputLayout firstNameLayout = findViewById(R.id.first_name_layout);
        TextInputLayout lastNameLayout = findViewById(R.id.last_name_layout);
        TextInputEditText lastName = findViewById(R.id.last_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(firstName.getText())) {
                    firstNameLayout.setError("Morate uneti ime");
                    return;
                }
                if (TextUtils.isEmpty(lastName.getText())) {
                    lastNameLayout.setError("Morate uneti prezime");
                    return;
                }

                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();

                createContactOnServer(firstNameText, lastNameText);
            }
        });
    }

    private void createContactOnServer(String firstNameText, String lastNameText) {
        new CreateContactAsyncTask(firstNameText, lastNameText).execute();
    }

    private class CreateContactAsyncTask extends NetworkAsyncTask<String, Integer, String> {

        private final String firstNameText;
        private final String lastNameText;

        public CreateContactAsyncTask(String firstNameText, String lastNameText) {
            this.firstNameText = firstNameText;
            this.lastNameText = lastNameText;
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject postData = new JSONObject();
            String stringUrl = "https://dashboard-admin-b00e5.firebaseio.com/contacts.json";
            try {
                postData.put("firstName", firstNameText);
                postData.put("lastName", lastNameText);
                return postRequest(stringUrl, postData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }
}