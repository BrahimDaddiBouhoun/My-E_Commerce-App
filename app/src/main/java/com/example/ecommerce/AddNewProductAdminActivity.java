package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class AddNewProductAdminActivity extends AppCompatActivity {

    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product_admin);

        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(AddNewProductAdminActivity.this, CategoryName, Toast.LENGTH_SHORT).show();
    }
}