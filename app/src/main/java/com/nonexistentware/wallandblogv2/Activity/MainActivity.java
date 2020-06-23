package com.nonexistentware.wallandblogv2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nonexistentware.wallandblogv2.Fragment.ArtistFragment;
import com.nonexistentware.wallandblogv2.Fragment.BlogFragment;
import com.nonexistentware.wallandblogv2.Fragment.DashboardFragment;
import com.nonexistentware.wallandblogv2.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_main_layout,
                    new DashboardFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.dashboard_menu:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.blog_menu:
                        fragment = new BlogFragment();
                        break;
                    case R.id.artist_menu:
                        fragment = new ArtistFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_main_layout,
                        fragment).commit();
                return true;
            }
        });

    }
}
