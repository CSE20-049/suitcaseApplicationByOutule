package com.example.suitcaseapplicationbyoutule;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.suitcaseapplicationbyoutule.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends Fragment {

    RecyclerView recyclerView;
    List<Item> itemList;

    ItemAdapter itemAdapter;

    DatabaseReference databaseReference;

    private String email;

    ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeActivityScreen = inflater.inflate(R.layout.fragment_home_activity, container,
                false);

        recyclerView = homeActivityScreen.findViewById(R.id.recyclerViewOne);

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

        // Retrieve Sanitized email from the intent.
        Intent intent = requireActivity().getIntent();
        email = intent.getStringExtra("email");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Creating a progress dialog and showing it
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        itemList = new ArrayList<>();
        itemAdapter =  new ItemAdapter(getContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(email)
                .child("Product");
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
            Intent intent1 = new Intent(requireContext(), AddNewItemActivity.class);
            intent1.putExtra("email", email);
            startActivity(intent1);
        });
        return homeActivityScreen;
    }
}