package com.example.suitcaseapplicationbyoutule;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suitcaseapplicationbyoutule.model.Item;
import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateItemActivity extends AppCompatActivity {

    TextInputEditText updateItemNameTextInputEditText, updateItemPriceTextInputEditText,
            updateItemDescriptionTextInputEditText;

    MaterialButton updateBTN;
    ShapeableImageView updateItemImageView;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri uri;

    private String oldImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        updateItemImageView = findViewById(R.id.updateItemImageView);
        updateItemNameTextInputEditText = findViewById(R.id.updateItemNameTextInputEditText);
        updateItemPriceTextInputEditText = findViewById(R.id.updateItemPriceTextInputEditText);
        updateItemDescriptionTextInputEditText = findViewById(R.id.updateItemDescriptionTextInputEditText);
        updateBTN = findViewById(R.id.updateBTN);

        // Retrieve Sanitized email from shared preference
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                "Email Preference", Context.MODE_PRIVATE);
        String email = preferences.getString("sanitizedEmail", "");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent data = o.getData();
                            uri = data.getData();
                            updateItemImageView.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateItemActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updateItemNameTextInputEditText.setText(bundle.getString("ItemName"));
            updateItemDescriptionTextInputEditText.setText(bundle.getString("Description"));
            updateItemPriceTextInputEditText.setText(bundle.getString("Price"));
            String key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
            databaseReference = FirebaseDatabase.getInstance().getReference("Items for " + email).child(key);
            Glide.with(this).load(oldImageURL).into(updateItemImageView);
        }

        updateItemImageView.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        updateBTN.setOnClickListener(view -> {
            saveData();
            Intent intent = new Intent(UpdateItemActivity.this, FragmentNavigatorActivity.class);
            startActivity(intent);
        });
    }

    public void saveData() {
        if (uri != null && uri.getLastPathSegment() != null && !uri.getLastPathSegment().isEmpty()) {
            // A new image has been selected, so upload it
            storageReference = FirebaseStorage.getInstance().getReference().child("Images/").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateItemActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.saving_dialog_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    updateItem(urlImage.toString());
                    dialog.dismiss();
                }
            });
        } else {
            // No new image selected, so just update the data with the existing image URL
            updateItem(null);
        }
    }

    public void updateItem(String imageUrl) {
        String itemName = updateItemNameTextInputEditText.getText().toString().trim();
        String itemDescription = updateItemDescriptionTextInputEditText.getText().toString().trim();
        String itemPrice = updateItemPriceTextInputEditText.getText().toString().trim();

        Item item = new Item();
        item.setName(itemName);
        item.setDescription(itemDescription);
        item.setPrice(itemPrice);

        // Only update the image URL if a new image is selected
        if (imageUrl != null) {
            item.setImage(imageUrl);
        } else {
            // If no new image is selected, retain the existing image URL
            item.setImage(oldImageURL);
        }

        // Using the existing key to update specific fields of the item
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", item.getName());
        updateMap.put("description", item.getDescription());
        updateMap.put("price", item.getPrice());
        updateMap.put("image", item.getImage());

        // Using updateChildren to update only specific fields without removing others
        databaseReference.updateChildren(updateMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully updated the item
                        Toast.makeText(UpdateItemActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Handle the update failure
                        Toast.makeText(UpdateItemActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the update failure
                    Toast.makeText(UpdateItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
