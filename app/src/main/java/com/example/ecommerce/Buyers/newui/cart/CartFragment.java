package com.example.ecommerce.Buyers.newui.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Buyers.BuyerCartActivity;
import com.example.ecommerce.Buyers.BuyerConfirmFinalOrderActivity;
import com.example.ecommerce.Buyers.BuyerHomeActivity;
import com.example.ecommerce.Buyers.BuyerProductsDetailsActivity;
import com.example.ecommerce.Listener.OnSwipeTouchListener;
import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.databinding.CartFragmentBinding;
import com.example.ecommerce.databinding.FragmentSellerHomeBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartFragment extends Fragment {

    private CartFragmentBinding binding;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,checkOutTxt;

    private int total= 0, totalFee = 0 ,delivery = 0, tax = 0 ;


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = CartFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.cardItemRv;
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        totalFeeTxt = binding.totalFeeTxt;
        taxTxt = binding.taxTxt;
        deliveryTxt = binding.deliveryTxt;

        totalTxt = binding.totalTxt;

        checkOutTxt = binding.checkOutTxt;
        checkOutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyerConfirmFinalOrderActivity.class);
                intent.putExtra("totalAmount",total);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.CurrentOnlineUser.getPhone()).child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.totalEachItem.setText(String.valueOf(Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity())));
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.plusTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int number;
                        number = Integer.parseInt(holder.txtProductQuantity.getText().toString());
                        if (number<10)
                        {
                            number = number +1;
                            holder.txtProductQuantity.setText(String.valueOf(number));
                            holder.totalEachItem.setText(String.valueOf(Integer.parseInt(model.getPrice()) * number ));

                            totalFee = totalFee + Integer.parseInt(model.getPrice());
                            totalFeeTxt.setText(String.valueOf(totalFee));

                            cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(model.getPid())
                                    .child("quantity").setValue(String.valueOf(number));

                            cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(model.getPid())
                                    .child("quantity").setValue(String.valueOf(number));
                        }

                    }
                });

                holder.minusTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int number;
                        number = Integer.parseInt(holder.txtProductQuantity.getText().toString());
                        if (number>1)
                        {
                            number = number -1;
                            holder.txtProductQuantity.setText(String.valueOf(number));
                            holder.totalEachItem.setText(String.valueOf(Integer.parseInt(model.getPrice()) * number ));

                            totalFee = totalFee - Integer.parseInt(model.getPrice());
                            totalFeeTxt.setText(String.valueOf(totalFee));

                            cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(model.getPid())
                                    .child("quantity").setValue(String.valueOf(number));

                            cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(model.getPid())
                                    .child("quantity").setValue(String.valueOf(number));

                        }
                    }
                });


                totalFee = totalFee + ((Integer.parseInt(model.getPrice())) * Integer.parseInt(model.getQuantity()));
                totalFeeTxt.setText(String.valueOf(totalFee));

                total = totalFee + delivery + tax ;
                totalTxt.setText(String.valueOf(total));


                holder.itemView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                    @Override
                    public void onSwipeLeft() {
                        cartListRef.child("Admin View")
                                .child(Prevalent.CurrentOnlineUser.getPhone())
                                .child("Products")
                                .child(model.getPid())
                                .removeValue();
                        cartListRef.child("User View")
                                .child(Prevalent.CurrentOnlineUser.getPhone())
                                .child("Products")
                                .child(model.getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Items removed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                holder.itemView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                    @Override
                    public void onSwipeRight() {
                        cartListRef.child("Admin View")
                                .child(Prevalent.CurrentOnlineUser.getPhone())
                                .child("Products")
                                .child(model.getPid())
                                .removeValue();
                        cartListRef.child("User View")
                                .child(Prevalent.CurrentOnlineUser.getPhone())
                                .child("Products")
                                .child(model.getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Items removed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_items, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}