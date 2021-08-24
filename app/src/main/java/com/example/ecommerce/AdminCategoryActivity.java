package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


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

        Button logout = findViewById(R.id.btn_admin_logout);
        Button checkOrders = findViewById(R.id.btn_check_orders);
        Button maintainProductBtn = findViewById(R.id.btn_maintain);

        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class );
            intent.putExtra("category","tShirts");
            startActivity(intent);
        });

        SportTShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","SportTShirts");
            startActivity(intent);
        });

        FemaleDresses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","FemaleDresses");
            startActivity(intent);
        });

        SweatHers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","SweatHers");
            startActivity(intent);
        });

        Glasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","Glasses");
            startActivity(intent);
        });

        HatsCaps.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","HatsCaps");
            startActivity(intent);
        });

        WalletsBagsPurses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","WalletBagsPurses");
            startActivity(intent);
        });

        Shoes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","Shoes");
            startActivity(intent);
        });

        HeadPhones.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","HeadPhones");
            startActivity(intent);
        });

        Laptops.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","Laptops");
            startActivity(intent);
        });

        Watches.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","Watches");
            startActivity(intent);
        });

        MobilePhones.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class );
            intent.putExtra("category","MobilePhones");
            startActivity(intent);
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class );
                startActivity(intent);
            }
        });

        maintainProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,HomeActivity.class );
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });


    }
}