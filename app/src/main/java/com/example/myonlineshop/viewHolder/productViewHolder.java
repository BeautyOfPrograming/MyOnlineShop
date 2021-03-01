package com.example.myonlineshop.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlineshop.R;

public class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductDescription;
    public ImageView imageView;

    public productViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.product_item_name);
        txtProductDescription = itemView.findViewById(R.id.product_item_description);
        imageView = itemView.findViewById(R.id.product_item_image);
    }

    @Override
    public void onClick(View view) {

    }
}
