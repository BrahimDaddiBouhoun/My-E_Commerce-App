package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity,totalEachItem;
    public ItemClickListner itemClickListner;
    public ImageView imageView,plusTxt,minusTxt;
    public ImageView delete;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.card_product_name);
        txtProductPrice = itemView.findViewById(R.id.card_product_price);
        txtProductQuantity = itemView.findViewById(R.id.card_product_quantity);
        imageView = itemView.findViewById(R.id.image_card_item);
        totalEachItem = itemView.findViewById(R.id.total_each_item);
        delete = itemView.findViewById(R.id.delete);

        plusTxt = itemView.findViewById(R.id.plusCardBtn);
        minusTxt = itemView.findViewById(R.id.minusCardBtn);

    }

    @Override
    public void onClick(View view)
    {

        itemClickListner.onClick(view, getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;

    }
}
