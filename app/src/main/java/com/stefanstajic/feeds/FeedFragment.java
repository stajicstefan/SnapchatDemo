package com.stefanstajic.feeds;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stefanstajic.R;
import com.stefanstajic.network.NetworkAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeedFragment extends Fragment {
    private final FeedListAdapter adapter = new FeedListAdapter(new ArrayList<>());

    public static FeedFragment newInstance() {

        Bundle args = new Bundle();

        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetFeedAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        return view;
    }


    class GetFeedAsyncTask extends NetworkAsyncTask<Void, Void, List<Feed>> {

        @Override
        protected List<Feed> doInBackground(Void... voids) {
            String stringUrl = "https://dashboard-admin-b00e5.firebaseio.com/feed.json";
            return convertToFeed(getRequest(stringUrl));
        }

        private List<Feed> convertToFeed(String response) {
            List<Feed> result = new ArrayList<>();
            try {
                JSONObject feedObject = new JSONObject(response);
                Iterator<String> iterator = feedObject.keys();
                while (iterator.hasNext()) {
                    JSONObject data = feedObject.getJSONObject(iterator.next());
                    Feed feed = new Feed();
                    feed.setCaption(data.getString("caption"));
                    feed.setImageUrl(data.getString("imageUrl"));
                    result.add(feed);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Feed> result) {
            adapter.setFeed(result);
        }
    }

}
