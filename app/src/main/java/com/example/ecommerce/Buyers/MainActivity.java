package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Users;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerHomeActivity;
import com.example.ecommerce.Sellers.SellerRegistrationActivity;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button JoinNowButton,LoginButton;
    private ProgressDialog LoadingBar;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JoinNowButton = findViewById(R.id.btn_join) ;
        LoginButton = findViewById(R.id.btn_login) ;

        sellerBegin = findViewById(R.id.seller_begin);

        LoadingBar = new ProgressDialog(this);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        JoinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistreActivity.class);
                startActivity(intent);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey!= "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                LoadingBar.setTitle("Already Logged in");
                LoadingBar.setMessage("Please wait...");
                LoadingBar.setCanceledOnTouchOutside(false);
                LoadingBar.show();
            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseuser != null)
        {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void AllowAccess(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("users").child(phone).exists())
                {
                    Users usersData = snapshot.child("users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                                Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.CurrentOnlineUser = usersData;
                                startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                        }

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "This Account with this "+phone+ " do not exists", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
}