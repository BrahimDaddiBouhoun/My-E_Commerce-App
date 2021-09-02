package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.LoginActivity;
import com.example.ecommerce.MainActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BuyerRegisterActivity extends AppCompatActivity {

    private EditText UserName,Password,PhoneNumber;
    private Button CreateAccount;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registre);

        CreateAccount = findViewById(R.id.btn_login);
        UserName = findViewById(R.id.ed_register_user_name);
        Password = findViewById(R.id.ed_login_password);
        PhoneNumber = findViewById(R.id.ed_login_phone);
        LoadingBar = new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 CreateAccount();

             }
         });
    }

    private void CreateAccount() {

        String name = UserName.getText().toString().trim();
        String phone = PhoneNumber.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "please write your name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "please write your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please write your password", Toast.LENGTH_SHORT).show();
        }

        LoadingBar.setTitle("Creating Account");
        LoadingBar.setMessage("Please wait, while we are checking the credential");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        ValidatePhoneNumber( name ,phone , password );
    }

    private void ValidatePhoneNumber(String name, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful())
                                 {
                                     Toast.makeText(BuyerRegisterActivity.this, "Congratulations, your account has been created. ", Toast.LENGTH_SHORT).show();
                                     LoadingBar.dismiss();

                                     Intent intent = new Intent(BuyerRegisterActivity.this, LoginActivity.class);
                                     startActivity(intent);

                                 }
                                 else
                                 {
                                     LoadingBar.dismiss();
                                     Toast.makeText(BuyerRegisterActivity.this, "Network Error: please try again", Toast.LENGTH_SHORT).show();
                                 }
                                }
                            });

                }
                else
                {
                    Toast.makeText(BuyerRegisterActivity.this, "This phone number "+ phone +"already exists", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                    Toast.makeText(BuyerRegisterActivity.this, "you can try to login ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BuyerRegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}