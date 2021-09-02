package com.example.ecommerce.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecommerce.R;

public class BuyerResetPasswordActivity extends AppCompatActivity {

    private String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_reset_password);

        check = getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (check.equals("setting"))
        {

        }
        else if (check.equals("login"))
        {

        }
    }
}