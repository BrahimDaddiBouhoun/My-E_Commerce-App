package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class OrderStateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView username;
    public RecyclerView orderStateRV;
    public StateProgressBar stateProgressBar;
    public ItemClickListner listner;


    public OrderStateViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.os_user_name);
        orderStateRV = itemView.findViewById(R.id.recyclerView_os);
        stateProgressBar = itemView.findViewById(R.id.your_state_progress_bar_id);


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
