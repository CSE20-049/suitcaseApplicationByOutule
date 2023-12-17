package com.example.suitcaseapplicationbyoutule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class ItemDetailActivity extends AppCompatActivity {

    ShapeableImageView imageView;
    MaterialTextView itemName, itemPrice, itemDescription;

    String key = "";
    String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        imageView = findViewById(R.id.itemDetailImageView);
        itemName = findViewById(R.id.itemDetailItemName);
        itemPrice = findViewById(R.id.itemDetailItemPrice);
        itemDescription =  findViewById(R.id.itemDetailItemDescription);

        //Going back to previous activity on navigation back arrow clicked
        MaterialToolbar detailsPageToolbar = findViewById(R.id.detailsPageToolbar);
        detailsPageToolbar.setNavigationOnClickListener(View -> {
            detailsPageToolbar.setNavigationIconTint(getColor(R.color.magenta_pink));
            finish();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName.setText(bundle.getString("ItemName"));
            itemDescription.setText(bundle.getString("Description"));
            itemPrice.setText(bundle.getString("Price"));
            key = bundle.getString("Key");
            imageURL = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(imageView);
        }

    }
}