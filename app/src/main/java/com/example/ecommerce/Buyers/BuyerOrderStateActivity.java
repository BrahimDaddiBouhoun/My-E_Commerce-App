package com.example.ecommerce.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecommerce.R;

public class BuyerOrderStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        displayOrderState();
    }

    private void displayOrderState() {
    }
}