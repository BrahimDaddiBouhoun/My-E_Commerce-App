package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Buyers.newui.home.HomeFragment;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.R;
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

public class BuyerProductsDetailsActivity extends AppCompatActivity {

    private ImageView productImage,plusBtn,minusBtn;

    private TextView productPrice, productDescription, productName,addToCartBtn,numberTxt;

    private int numberOrder= 1 ;


    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_products_details);

        productID = getIntent().getStringExtra("pid");

        numberTxt = findViewById(R.id.btn_number);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        addToCartBtn = findViewById(R.id.btn_add_to_cart);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);

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
                    Toast.makeText(BuyerProductsDetailsActivity.this, "you can add products once yours are shipped", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        CheckOrdersState();
//    }

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
                                             Toast.makeText(BuyerProductsDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                             Intent intent = new Intent(BuyerProductsDetailsActivity.this, HomeActivity.class);
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
                    Picasso.get().load(products.getImage()).into(productImage);
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