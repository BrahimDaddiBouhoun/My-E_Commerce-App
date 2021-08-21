package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEdittext, addressEditText,cityEditText;
    private Button confirmOrderButton;

    private String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderButton = findViewById(R.id.btn_confirm_order);
        nameEditText = findViewById(R.id.shipment_name);
        phoneEdittext = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);


    }
}