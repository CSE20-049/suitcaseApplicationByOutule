package com.example.suitcaseapplicationbyoutule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders.FragmentNavigatorActivityAdapter;
import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FragmentNavigatorActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_navigator);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        FragmentNavigatorActivityAdapter fragmentNavigatorActivityAdapter = new
                FragmentNavigatorActivityAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(fragmentNavigatorActivityAdapter);

        //Link bottomNavigationView with viewPager 2
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeIconID) {
                    viewPager2.setCurrentItem(0, true);
                    return true;
                } else if (itemId == R.id.purchasedIconID) {
                    viewPager2.setCurrentItem(1, true);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Add a listener to handle page changes in the ViewPager2
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Update the selected item in the BottomNavigationView when the page changes
                bottomNavigationView.setSelectedItemId(getBottomNavigationItemId(position));
            }
        });

    }

    // Helper method to map ViewPager2 position to BottomNavigationView item ID
    private int getBottomNavigationItemId(int position) {
        switch (position) {
            case 0:
                return R.id.homeIconID;
            case 1:
                return R.id.purchasedIconID;
            default:
                return 0;
        }
    }

}