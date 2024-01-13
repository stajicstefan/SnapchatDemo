package com.stefanstajic;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager2 viewPager = findViewById(R.id.content_pager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager.setAdapter(new ContentPager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo_testing, menu);
        return true;
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

    static class ContentPager extends FragmentStateAdapter {

        public ContentPager(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 2) {
                return DemoMapsFragment.newInstance();
            }
            return DemoItemFragment.newInstance(0);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}