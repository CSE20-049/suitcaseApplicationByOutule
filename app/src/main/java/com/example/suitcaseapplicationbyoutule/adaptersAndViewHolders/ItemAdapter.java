package com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.suitcaseapplicationbyoutule.FragmentNavigatorActivity;
import com.example.suitcaseapplicationbyoutule.ItemDetailActivity;
import com.example.suitcaseapplicationbyoutule.R;
import com.example.suitcaseapplicationbyoutule.ShareItemDialog;
import com.example.suitcaseapplicationbyoutule.UpdateItemActivity;
import com.example.suitcaseapplicationbyoutule.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private Context context;
    private List<Item> itemList;

    private DatabaseReference databaseReference;

    private androidx.fragment.app.FragmentManager fragmentManager;

    private String email;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public ItemAdapter(Context context, List<Item> itemList, FragmentManager fragmentManager) {
        this.context = context;
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_home_activity_blueprint, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Glide.with(context).load(itemList.get(position).getImage()).into(holder.imageView);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemDescription.setText(itemList.get(position).getDescription());
        holder.homeActivityCheckBox.isChecked();

        // Retrieve the value of "isChecked" from the Firebase Realtime Database
        String itemKey = itemList.get(position).getKey();

        // Retrieve Sanitized email from shared preference
        SharedPreferences preferences = context.getSharedPreferences(
                "Email Preference", Context.MODE_PRIVATE);
        email = preferences.getString("sanitizedEmail", "");

        DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("Items for " + email).child(itemKey);
        itemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isChecked = dataSnapshot.child("isChecked").getValue(Boolean.class);

                    // Set the checkbox state based on the value of "isChecked"
                    holder.homeActivityCheckBox.setChecked(isChecked);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("Image", itemList.get(holder.getAdapterPosition()).getImage());
            intent.putExtra("Description", itemList.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("ItemName", itemList.get(holder.getAdapterPosition()).getName());
            intent.putExtra("Price", itemList.get(holder.getAdapterPosition()).getPrice());
            intent.putExtra("Key",itemList.get(holder.getAdapterPosition()).getKey());

            context.startActivity(intent);
        });

        holder.editIcon.setOnClickListener(View -> {
            Intent intent = new Intent(context, UpdateItemActivity.class);
            intent.putExtra("Image", itemList.get(holder.getAdapterPosition()).getImage());
            intent.putExtra("Description", itemList.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("ItemName", itemList.get(holder.getAdapterPosition()).getName());
            intent.putExtra("Price", itemList.get(holder.getAdapterPosition()).getPrice());
            intent.putExtra("Key",itemList.get(holder.getAdapterPosition()).getKey());

            context.startActivity(intent);
        });

        holder.deleteIcon.setOnClickListener(view -> {

            // Retrieve Sanitized email from shared preference
            email = preferences.getString("sanitizedEmail", "");

            // Obtain the item's key and position
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && !itemList.isEmpty() && adapterPosition < itemList.size()) {


                // Get the image URL associated with the item
                String imageUrl = itemList.get(adapterPosition).getImage();

                // Remove the item from the Firebase Realtime Database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Items for " + email).child(itemKey);
                reference.removeValue()
                        .addOnSuccessListener(aVoid -> {

                            // Delete the image from Firebase Storage
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                            storageReference.delete()
                                    .addOnSuccessListener(aVoid1 -> {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        // Start the new activity here
                                        Intent intent = new Intent(context, FragmentNavigatorActivity.class);
                                        context.startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure to delete image
                                    });

                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                        });
            } else {
                // If adapterPosition is equal to RecyclerView.NO_POSITION or greater than or equal to the size of itemList,
                // start FragmentNavigatorActivity
                Intent intent = new Intent(context, FragmentNavigatorActivity.class);
                context.startActivity(intent);
            }
        });

        holder.shareIcon.setOnClickListener(View -> {
            openDialog();
        });

        // Marking item as purchased and un-marking it whilst updating it's field at the database
        holder.homeActivityCheckBox.setOnClickListener(view -> {
            // Retrieve Sanitized email from shared preference
           email = preferences.getString("sanitizedEmail", "");

            // Obtain the item's position
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && !itemList.isEmpty() && adapterPosition < itemList.size()) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Items for " + email)
                        .child(itemKey);

                boolean isChecked = holder.homeActivityCheckBox.isChecked();

                // Using the existing key to update the "isChecked" field of the item
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("isChecked", isChecked);

                // Using updateChildren to update only the "isChecked" field without removing others
                databaseReference.updateChildren(updateMap)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Successfully updated the "isChecked" field of the item
                                Toast.makeText(context, isChecked ? "Purchased" : "Not Purchased", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle the update failure
                                Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle the update failure
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // If adapterPosition is equal to RecyclerView.NO_POSITION or greater than or equal to the size of itemList,
                // display a message or take appropriate action
                Toast.makeText(context, "Invalid item position", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void searchDataList(ArrayList<Item> itemArrayList){
        itemList = itemArrayList;
        notifyDataSetChanged();
    }

    public void openDialog() {
        ShareItemDialog shareItemDialog = new ShareItemDialog();
        shareItemDialog.show(fragmentManager, "dialog");
    }

}
