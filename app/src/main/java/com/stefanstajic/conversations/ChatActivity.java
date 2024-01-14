package com.stefanstajic.conversations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.stefanstajic.R;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String chatId;
    private RecyclerView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View send = findViewById(R.id.send);
        TextInputEditText message = findViewById(R.id.message);
        messageList = findViewById(R.id.message_list);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        String name = getIntent().getStringExtra("name");
        chatId = getIntent().getStringExtra("id");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send.setOnClickListener(view -> {
            String text = message.getText().toString();
            new SendMessageAsyncTask(chatId, text).execute();
            message.setText("");
        });
        messageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        messageList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        new GetMessagesAsyncTask(chatId).execute();
    }

    class GetMessagesAsyncTask extends NetworkAsyncTask<String, Integer, List<Message>> {

        private final String chatId;

        public GetMessagesAsyncTask(String chatId) {
            this.chatId = chatId;
        }

        @Override
        protected List<Message> doInBackground(String... params) {
            String url = "https://dashboard-admin-b00e5.firebaseio.com/messages/" + chatId + ".json";
            System.out.println(url);
            return convertToMessages(getRequest(url));
        }

        private List<Message> convertToMessages(String result) {
            List<Message> messages = new ArrayList<>();
            try {
                JSONObject data = new JSONObject(result);
                Iterator<String> keys = data.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject messageObject = data.getJSONObject(key);
                    Message message = new Message();
                    message.setText(messageObject.getString("text"));
                    message.setTimestamp(messageObject.getLong("timestamp"));
                    messages.add(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Collections.reverse(messages);
            return messages;
        }

        @Override
        protected void onPostExecute(List<Message> result) {
            System.out.println(result);
            messageList.setAdapter(new MessagesListAdapter(result));
        }
    }

    class SendMessageAsyncTask extends NetworkAsyncTask<String, Integer, String> {

        private final String text;
        private final String chatId;

        public SendMessageAsyncTask(String chatId, String text) {
            this.chatId = chatId;
            this.text = text;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dashboard-admin-b00e5.firebaseio.com/messages/" + chatId + ".json";
            JSONObject payload = new JSONObject();
            try {
                payload.put("text", text);
                payload.put("timestamp", System.currentTimeMillis());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return postRequest(url, payload);
        }

        @Override
        protected void onPostExecute(String s) {
            new GetMessagesAsyncTask(chatId).execute();
        }
    }
}