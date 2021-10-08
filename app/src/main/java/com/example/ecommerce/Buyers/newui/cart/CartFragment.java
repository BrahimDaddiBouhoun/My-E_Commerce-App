package com.example.ecommerce.Buyers.newui.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminNewOrdersActivity;
import com.example.ecommerce.Buyers.BuyerConfirmFinalOrderActivity;
import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Buyers.newui.productdetails.ProductDetailsFragment;
import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.databinding.BuyerCartFragmentBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CartFragment extends Fragment {

    private BuyerCartFragmentBinding binding;
    private boolean clicked = false;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,checkOutTxt,message,cartText;
    private ImageView emptyCart;
    private Button receiveProduct;
    private int total= 0, totalFee = 0 ,delivery = 0, tax = 0 ;

    private LinearLayout layout;


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BuyerCartFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.cardItemRv;
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        layout = binding.layout;


        totalFeeTxt = binding.totalFeeTxt;
        taxTxt = binding.taxTxt;
        deliveryTxt = binding.deliveryTxt;

        totalTxt = binding.totalTxt;

        message = binding.message;
        cartText = binding.carttext;

        emptyCart = binding.emptycart;

        checkOutTxt = binding.checkOutTxt;
        checkOutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyerConfirmFinalOrderActivity.class);
                intent.putExtra("totalAmount",total);
                startActivity(intent);
            }
        });

        receiveProduct = binding.receiveProduct;
        receiveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientConfirmProductDelivery();
            }
        });

        return root;
    }

    private void ClientConfirmProductDelivery() {
        final DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child("Pending")
                .child(Prevalent.CurrentOnlineUser.getPhone())
                .child("client confirmation");

        CharSequence options[] = new CharSequence[]
                {
                        "Yes",
                        "No"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure that you have received all your product?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    pendingRef.setValue("ok");
                    Toast.makeText(getActivity(), "we hope that our service satisfied you", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please confirm your orders once you receive them", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        totalFee=0;
        clicked = false;
        CheckOrdersState();

        displayCartItems();
    }


    private void displayCartItems() {
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

                        clicked=true;

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
                        }

                    }
                });

                holder.minusTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = true;

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

                        }
                    }
                });

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", model.getPid());

                        ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                        fragment2.setArguments(bundle);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.buyer_fragment_host, fragment2).addToBackStack(null)
                                .commit();

                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        totalFee = totalFee - Integer.parseInt(model.getPrice())*Integer.parseInt(model.getQuantity());
                        totalFeeTxt.setText(String.valueOf(totalFee));

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

                if(!clicked) {

                    totalFee = totalFee + ((Integer.parseInt(model.getPrice())) * Integer.parseInt(model.getQuantity()));
                    totalFeeTxt.setText(String.valueOf(totalFee));
                }
                total = totalFee + delivery + tax;
                totalTxt.setText(String.valueOf(total));
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

    private void CheckOrdersState()
    {
        final DatabaseReference userViewRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products");
        final DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child("Pending").child(Prevalent.CurrentOnlineUser.getPhone());

        userViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    cartText.setVisibility(View.GONE);
                    emptyCart.setVisibility(View.GONE);
                    message.setVisibility(View.GONE);
                    receiveProduct.setVisibility(View.GONE);

                    layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    pendingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                if (snapshot.child("client confirmation").getValue().equals("not ok")) {
                                    String userName = snapshot.child("name").getValue().toString();

                                    layout.setVisibility(View.GONE);
                                    cartText.setVisibility(View.GONE);
                                    emptyCart.setVisibility(View.GONE);

                                    receiveProduct.setVisibility(View.VISIBLE);
                                    message.setVisibility(View.VISIBLE);
                                    message.setText("Dear " + userName +
                                            "\n your order still not have been shipped" +
                                            "\n you can purchase more products, once you receive your order");
                                }
                            }
                            else
                            {
                                cartText.setVisibility(View.VISIBLE);
                                emptyCart.setVisibility(View.VISIBLE);

                                message.setVisibility(View.GONE);
                                receiveProduct.setVisibility(View.GONE);
                                layout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String userName = snapshot.child("name").getValue().toString();

                        layout.setVisibility(View.GONE);
                        cartText.setVisibility(View.GONE);
                        emptyCart.setVisibility(View.GONE);

                        message.setVisibility(View.VISIBLE);
                        message.setText("Dear " + userName +
                                "\n your product still not have been shipped" +
                                "\n you can purchase more products, once you receive your order");


                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}