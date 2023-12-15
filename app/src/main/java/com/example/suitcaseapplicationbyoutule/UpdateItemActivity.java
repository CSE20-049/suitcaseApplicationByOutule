package com.example.suitcaseapplicationbyoutule;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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

public class UpdateItemActivity extends AppCompatActivity {

    ShapeableImageView updateItemImageView;
    TextInputEditText updateItemNameTextInputEditText, updateItemPriceTextInputEditText,
            updateItemDescriptionTextInputEditText;

    String title, price, description, imageURL, key, oldImageURL;

    MaterialButton updateBTN;

    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

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
            key = bundle.getString("Key");
            imageURL = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(updateItemImageView);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Product").child(key);

        updateItemImageView.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });
        updateBTN.setOnClickListener(view ->  {
            saveData();
            Intent intent = new Intent(UpdateItemActivity.this, MainActivity.class);
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
                    imageURL = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            });
        } else {
            // Handle the case where the URI is null or empty
            Toast.makeText(this, "Invalid image URI", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateData() {

        if (imageURL != null) {
            title = updateItemNameTextInputEditText.getText().toString().trim();
            description =  updateItemDescriptionTextInputEditText.getText().toString().trim();
            price = updateItemPriceTextInputEditText.getText().toString().trim();

            Item item = new Item();
            item.setName(title);
            item.setDescription(description);
            item.setPrice(price);
            item.setImage(imageURL);

            databaseReference.setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                        Toast.makeText(UpdateItemActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateItemActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            title = updateItemNameTextInputEditText.getText().toString().trim();
            description =  updateItemDescriptionTextInputEditText.getText().toString().trim();
            price = updateItemPriceTextInputEditText.getText().toString().trim();

            Item item = new Item();
            item.setName(title);
            item.setDescription(description);
            item.setPrice(price);
            item.setImage(oldImageURL);

            databaseReference.setValue(title).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                        Toast.makeText(UpdateItemActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateItemActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}