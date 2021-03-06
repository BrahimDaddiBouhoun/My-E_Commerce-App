package com.example.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteProductBtn ;
    private EditText name,price,description;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_maintain_products);

        applyChangesBtn = findViewById(R.id.btn_seller_apply_changes_maintain);
        deleteProductBtn = findViewById(R.id.btn_seller_delete_product);

        name = findViewById(R.id.seller_product_name_maintain);
        price = findViewById(R.id.seller_product_price_maintain);
        description = findViewById(R.id.seller_product_description_maintain);

        imageView = findViewById(R.id.seller_product_image_maintain);

        productID = getIntent().getStringExtra("pid");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        displaySpecificInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void displaySpecificInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void applyChanges()
    {
        String pName = name.getText().toString().trim();
        String pPrice = price.getText().toString().trim();
        String pDescription = description.getText().toString().trim();

        if (pName.equals(null) || pPrice.equals(null) || pDescription.equals(null))
        {
            Toast.makeText(SellerMaintainProductsActivity.this, "Please write down all the fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap =new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(SellerMaintainProductsActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void deleteProduct()
    {
                productRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerMaintainProductsActivity.this, "That item has been Deleted successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}