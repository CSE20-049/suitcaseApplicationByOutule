package com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcaseapplicationbyoutule.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class PurchasedItemViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView purchasedActivityImageView;
    MaterialTextView purchasedActivityItemName;

    public PurchasedItemViewHolder(@NonNull View itemView) {
        super(itemView);

        purchasedActivityImageView = itemView.findViewById(R.id.purchasedActivityImageView);
        purchasedActivityItemName = itemView.findViewById(R.id.purchasedActivityItemName);

    }
}
