package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, SportTShirts, FemaleDresses, SweatHers;
    private ImageView Glasses, HatsCaps, WalletsBagsPurses, Shoes;
    private ImageView HeadPhones, Laptops, Watches, MobilePhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        tShirts = findViewById(R.id.iv_tshirts);
        SportTShirts = findViewById(R.id.iv_sports);
        FemaleDresses = findViewById(R.id.iv_female_dresses);
        SweatHers = findViewById(R.id.iv_sweather);

        Glasses = findViewById(R.id.iv_glasses);
        HatsCaps = findViewById(R.id.iv_hats);
        WalletsBagsPurses = findViewById(R.id.iv_purses_bags);
        Shoes = findViewById(R.id.iv_shoess);

        HeadPhones = findViewById(R.id.iv_headphoness);
        Laptops = findViewById(R.id.iv_laptops);
        Watches = findViewById(R.id.iv_watches);
        MobilePhones = findViewById(R.id.iv_mobiles);

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });

        SportTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","SportTShirts");
                startActivity(intent);
            }
        });

        FemaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","FemaleDresses");
                startActivity(intent);
            }
        });

        SweatHers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","SweatHers");
                startActivity(intent);
            }
        });

        Glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        HatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","HatsCaps");
                startActivity(intent);
            }
        });

        WalletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","WalletBagsPurses");
                startActivity(intent);
            }
        });

        Shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        HeadPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","HeadPhones");
                startActivity(intent);
            }
        });

        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        Watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        MobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AddNewProductAdminActivity.class );
                intent.putExtra("category","MobilePhones");
                startActivity(intent);
            }
        });


    }
}