package com.example.ecommerce.ViewHolder;

import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.ecommerce.Interface.ItemClickListner;
        import com.example.ecommerce.R;

public class ItemSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductState;
    public ImageView imageView;
    public ItemClickListner listner;


    public ItemSellerViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView = itemView.findViewById(R.id.product_seller_image );
        txtProductName = itemView.findViewById(R.id.product_seller_name );
        txtProductDescription = itemView.findViewById(R.id.product_seller_description );
        txtProductPrice = itemView.findViewById(R.id.product_seller_price );
        txtProductState = itemView.findViewById(R.id.product_seller_state);

    }

    public void setItemClickListner(ItemClickListner Listner)
    {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view,getAdapterPosition(),false);

    }
}

