package com.example.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.ecommerce.R;

public class SellerProductCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);


        ImageView tShirts = findViewById(R.id.iv_tshirts);
        ImageView SportTShirts = findViewById(R.id.iv_sports);
        ImageView FemaleDresses = findViewById(R.id.iv_female_dresses);
        ImageView SweatHers = findViewById(R.id.iv_sweather);

        ImageView Glasses = findViewById(R.id.iv_glasses);
        ImageView HatsCaps = findViewById(R.id.iv_hats);
        ImageView WalletsBagsPurses = findViewById(R.id.iv_purses_bags);
        ImageView Shoes = findViewById(R.id.iv_shoess);

        ImageView HeadPhones = findViewById(R.id.iv_headphoness);
        ImageView Laptops = findViewById(R.id.iv_laptops);
        ImageView Watches = findViewById(R.id.iv_watches);
        ImageView MobilePhones = findViewById(R.id.iv_mobiles);


        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","tShirts");
            startActivity(intent);
        });

        SportTShirts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","SportTShirts");
            startActivity(intent);
        });

        FemaleDresses.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","FemaleDresses");
            startActivity(intent);
        });

        SweatHers.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","SweatHers");
            startActivity(intent);
        });

        Glasses.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","Glasses");
            startActivity(intent);
        });

        HatsCaps.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","HatsCaps");
            startActivity(intent);
        });

        WalletsBagsPurses.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","WalletBagsPurses");
            startActivity(intent);
        });

        Shoes.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","Shoes");
            startActivity(intent);
        });

        HeadPhones.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","HeadPhones");
            startActivity(intent);
        });

        Laptops.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","Laptops");
            startActivity(intent);
        });

        Watches.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","Watches");
            startActivity(intent);
        });

        MobilePhones.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class );
            intent.putExtra("category","MobilePhones");
            startActivity(intent);
        });


    }
}