package com.stefanstajic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

    private class CreateContactAsyncTask extends AsyncTask<String, Integer, String> {

        private final String firstNameText;
        private final String lastNameText;

        public CreateContactAsyncTask(String firstNameText, String lastNameText) {
            this.firstNameText = firstNameText;
            this.lastNameText = lastNameText;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection;
            try {
                JSONObject postData = new JSONObject();
                postData.put("firstName", firstNameText);
                postData.put("lastName", lastNameText);
                URL url = new URL("https://dashboard-admin-b00e5.firebaseio.com/contacts.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, StandardCharsets.UTF_8));
                writer.write(postData.toString());
                writer.flush();

                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Arrays.toString(strings));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }
}