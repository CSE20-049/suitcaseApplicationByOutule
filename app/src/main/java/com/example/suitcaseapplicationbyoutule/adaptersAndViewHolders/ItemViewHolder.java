package com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcaseapplicationbyoutule.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView imageView, editIcon, deleteIcon, shareIcon;
    MaterialTextView itemName, itemDescription;

    MaterialCardView cardView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.homeActivityImageView);
        itemName = itemView.findViewById(R.id.homeActivityItemName);
        itemDescription = itemView.findViewById(R.id.homeActivityDescriptionTextView);
        cardView = itemView.findViewById(R.id.homeActivityCardView);
        editIcon = itemView.findViewById(R.id.homeActivityEditIcon);
        deleteIcon = itemView.findViewById(R.id.homeActivityDeleteIcon);
        shareIcon = itemView.findViewById(R.id.homeActivityShareIcon);

    }
}
