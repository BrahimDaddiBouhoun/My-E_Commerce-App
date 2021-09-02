package com.example.ecommerce.Buyers.newui.productdetails;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ecommerce.Buyers.BuyerProductsDetailsActivity;
import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.FragmentProductDetailsBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;

    private ImageView productImage,plusBtn,minusBtn;

    private String image = "" ;

    private TextView productPrice, productDescription, productName,addToCartBtn,numberTxt;

    private int numberOrder= 1 ;


    private String productID = "", state = "Normal";

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        productID = getActivity().getIntent().getStringExtra("pid");

        Bundle bundle = this.getArguments();

        productID = bundle.getString("pid");

        numberTxt = binding.btnNumber;
        productImage = binding.productImageDetails;
        productPrice = binding.productPriceDetails;
        productDescription = binding.productDescriptionDetails;
        productName = binding.productNameDetails;
        addToCartBtn = binding.btnAddToCart;
        plusBtn = binding.plusBtn;
        minusBtn = binding.minusBtn;

        numberTxt.setText(String.valueOf(numberOrder));
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOrder<10) {
                    numberOrder = numberOrder + 1;
                }
                numberTxt.setText(String.valueOf(numberOrder));
            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOrder>1) {
                    numberOrder = numberOrder - 1;
                }
                numberTxt.setText(String.valueOf(numberOrder));
            }
        });

        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed"))
                {
                    Toast.makeText(getActivity(), "you can add products once yours are shipped", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

//        CheckOrdersState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString().replace(" $",""));
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",String.valueOf(numberOrder));
        cartMap.put("discount","");
        cartMap.put("image",image);

        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(), "Added to Cart List", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice() + " $");
                    productDescription.setText(products.getDescription());
                    image = products.getImage();
                    Picasso.get().load(image).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    private void CheckOrdersState()
//    {
//        DatabaseReference ordersRef;
//        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());
//
//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists())
//                {
//                    String shipmentState = snapshot.child("state").getValue().toString();
//
//
//                    if(shipmentState.equals("shipped"))
//                    {
//                        state= "Order Shipped";
//                    }
//                    else if (shipmentState.equals("not shipped"))
//                    {
//                        state = "Order Placed";
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}



