package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistreActivity extends AppCompatActivity {

    private EditText UserName,Password,PhoneNumber;
    private Button CreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);

        CreateAccount = (Button) findViewById(R.id.btn_create_account);
        UserName = (EditText) findViewById(R.id.ed_register_user_name);
        Password = (EditText) findViewById(R.id.ed_register_password);
        PhoneNumber = (EditText) findViewById(R.id.ed_register_phone);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
    }
}