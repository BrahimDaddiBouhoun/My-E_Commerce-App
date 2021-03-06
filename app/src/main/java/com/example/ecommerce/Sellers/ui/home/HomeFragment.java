package com.example.ecommerce.Sellers.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Models.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerMaintainProductsActivity;
import com.example.ecommerce.ViewHolder.ItemSellerViewHolder;
import com.example.ecommerce.databinding.FragmentSellerHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    private FragmentSellerHomeBinding binding;

    private RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference unverifiedProductsRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSellerHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView =binding.sellerHomeRecyclerview;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductsRef.orderByChild("sID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ItemSellerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ItemSellerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemSellerViewHolder holder, int position, @NonNull Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductState.setText("State : " + model.getProductState());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + " $");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        if (model.getProductState().equals("not approved"))
                        {
                            holder.txtProductState.setTextColor(Color.RED);
                        }
                        else
                        {
                            holder.txtProductState.setVisibility(View.INVISIBLE);
                        }

                        final Products itemClick = model ;
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String ProductID = model.getPid();

                                CharSequence options [] = new CharSequence[]
                                        {
                                                "yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Do you want to update this Product details or delete it ?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0)
                                        {

                                            Intent intent = new Intent(getActivity(), SellerMaintainProductsActivity.class);
                                            intent.putExtra("pid",ProductID);
                                            startActivity(intent);
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        ItemSellerViewHolder holder = new ItemSellerViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}