package com.example.suitcaseapplicationbyoutule.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.suitcaseapplicationbyoutule.HomeActivity;
import com.example.suitcaseapplicationbyoutule.ProfileActivity;
import com.example.suitcaseapplicationbyoutule.PurchasedActivity;

public class FragmentNavigatorActivityAdapter extends FragmentStateAdapter {

    public FragmentNavigatorActivityAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeActivity();
            case 1:
                return new PurchasedActivity();
            case 2:
                return new ProfileActivity();
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
