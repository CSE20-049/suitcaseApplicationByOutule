package com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.suitcaseapplicationbyoutule.ItemDetailActivity;
import com.example.suitcaseapplicationbyoutule.R;
import com.example.suitcaseapplicationbyoutule.UpdateItemActivity;
import com.example.suitcaseapplicationbyoutule.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
