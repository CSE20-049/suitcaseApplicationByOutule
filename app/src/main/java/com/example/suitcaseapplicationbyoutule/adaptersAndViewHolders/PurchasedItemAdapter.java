package com.example.suitcaseapplicationbyoutule.adaptersAndViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.suitcaseapplicationbyoutule.R;
import com.example.suitcaseapplicationbyoutule.model.Item;

import java.util.ArrayList;
import java.util.List;

public class PurchasedItemAdapter extends RecyclerView.Adapter<PurchasedItemViewHolder> {

    private Context context;
    private List<Item> itemList;

    public PurchasedItemAdapter (Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public PurchasedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchasedItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_purchased_activity_blueprint, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedItemViewHolder holder, int position) {
        Glide.with(context).load(itemList.get(position).getImage()).into(holder.purchasedActivityImageView);
        holder.purchasedActivityItemName.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void searchDataList(ArrayList<Item> itemArrayList){
        itemList = itemArrayList;
        notifyDataSetChanged();
    }
}
