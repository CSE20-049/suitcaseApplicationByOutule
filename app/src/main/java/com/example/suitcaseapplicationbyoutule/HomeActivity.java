package com.example.suitcaseapplicationbyoutule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders.ItemAdapter;
import com.example.suitcaseapplicationbyoutule.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    RecyclerView recyclerView;
    List<Item> itemList;

    ItemAdapter itemAdapter;

    DatabaseReference databaseReference;

    ValueEventListener valueEventListener;

    SearchView homeActivitySearchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeActivityScreen = inflater.inflate(R.layout.fragment_home_activity, container,
                false);

        recyclerView = homeActivityScreen.findViewById(R.id.recyclerViewOne);
        homeActivitySearchBar = homeActivityScreen.findViewById(R.id.homeActivitySearchBar);
        homeActivitySearchBar.clearFocus();

        homeActivitySearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        //This code below helps style the application's name by applying color gradient.
        MaterialTextView listHubTextView = homeActivityScreen.findViewById(R.id.listHubTextView);
        TextPaint textPaint = listHubTextView.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, listHubTextView.getTextSize(),
                new int[] {
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        listHubTextView.getPaint().setShader(textShader);

        /* Using ColorStateList resource file to define different colors for different states for the
        floatingBTN icon*/
        FloatingActionButton addBTN = homeActivityScreen.findViewById(R.id.addFabBTN);
        ColorStateList colorStateList = ContextCompat.getColorStateList(requireContext(),
                R.color.fab_icon_tint);
        addBTN.setImageTintList(colorStateList);

        // Retrieve Sanitized email from shared preference
        SharedPreferences preferences = requireActivity().getApplicationContext().getSharedPreferences(
                "Email Preference", Context.MODE_PRIVATE);
        String email = preferences.getString("sanitizedEmail", "");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Creating a progress dialog and showing it
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        itemList = new ArrayList<>();
        itemAdapter =  new ItemAdapter(getContext(), itemList, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Items for " + email);
        dialog.show();

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    item.setKey(itemSnapshot.getKey());
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        //floatingBTN ActionLister__________________________________________________________________
        addBTN.setOnClickListener(View -> {
            Intent intent = new Intent(requireContext(), AddNewItemActivity.class);
            startActivity(intent);
        });
        return homeActivityScreen;
    }

    //Search for a specific Item in the list
    private void searchList(String searchText) {

        ArrayList<Item> itemArrayList = new ArrayList<>();
        for (Item item: itemList) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                itemArrayList.add(item);
            }
        }
        itemAdapter.searchDataList(itemArrayList);
    }
}