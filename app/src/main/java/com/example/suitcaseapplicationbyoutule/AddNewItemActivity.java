package com.example.suitcaseapplicationbyoutule;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Toast;

import com.example.suitcaseapplicationbyoutule.model.Item;
import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNewItemActivity extends AppCompatActivity {

    ShapeableImageView itemImage;
    TextInputEditText itemNameField, itemPriceField, itemDescriptionField;

    MaterialButton saveBTN;

    String imageURL;

    Uri uri;

    private String email;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        itemImage = findViewById(R.id.newItemimage);
        itemNameField = findViewById(R.id.newItemNameTextInputEditText);
        itemPriceField = findViewById(R.id.newItemPriceTextInputEditText);
        itemDescriptionField = findViewById(R.id.newItemDescriptionTextInputEditText);
        saveBTN = findViewById(R.id.saveBTN);

        // Retrieve Sanitized email from shared preference
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                "Email Preference", Context.MODE_PRIVATE);
        email = preferences.getString("sanitizedEmail", "");

        MaterialToolbar materialToolbar = findViewById(R.id.newItemToolBar);
        materialToolbar.setNavigationOnClickListener(View -> {
            materialToolbar.setNavigationIconTint(getColor(R.color.magenta_pink));
            finish();
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            itemImage.setImageURI(uri);
                        } else {
                            Toast.makeText(AddNewItemActivity.this, "No Image Selected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        itemImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        saveBTN.setOnClickListener(view -> {
            saveData();
        });

    }

    private void saveData() {
        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("Images")
                .child(uri.getLastPathSegment());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog
                .Builder(AddNewItemActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.saving_dialog_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uriImage = uriTask.getResult();
                imageURL = uriImage.toString();
                uploadData();
                //pending
                dialog.dismiss();
            }
        });
    }

    public void uploadData() {
        String itemName = itemNameField.getText().toString().trim();
        String itemPrice = itemPriceField.getText().toString().trim();
        String itemDescription = itemDescriptionField.getText().toString().trim();


        Item item = new Item();
        item.setName(itemName);
        item.setPrice(itemPrice);
        item.setDescription(itemDescription);
        item.setImage(imageURL);
        item.setIsChecked(false);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference( "Items for " + email).child(currentDate)
                .setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddNewItemActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}