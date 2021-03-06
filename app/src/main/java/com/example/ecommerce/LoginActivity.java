package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Models.Users;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText LoginPhone,LoginPassword;
    private Button Login;
    private ProgressDialog LoadingBar;
    private String ParentDbName = "users";
    private TextView IamAdmin,IamNotAdmin;
    private CheckBox checkBoxRememberMe ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_login);

        LoginPhone = findViewById(R.id.ed_login_phone);
        LoginPassword = findViewById(R.id.ed_login_password);
        Login = findViewById(R.id.btn_login);
        IamAdmin = findViewById(R.id.tv_iam_admin);
        IamNotAdmin = findViewById(R.id.tv_not_admin);

        checkBoxRememberMe = findViewById(R.id.checkBox);
        Paper.init(this);

        LoadingBar = new ProgressDialog(LoginActivity.this);

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey!= "") {
            LoginPhone.setText(UserPhoneKey);
            LoginPassword.setText(UserPasswordKey);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();
            }
        });


        IamAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setText("Login Admin");
                IamAdmin.setVisibility(View.INVISIBLE);
                IamNotAdmin.setVisibility(View.VISIBLE);
                ParentDbName = "Admins";
            }
        });

        IamNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setText("Login");
                IamAdmin.setVisibility(View.VISIBLE);
                IamNotAdmin.setVisibility(View.INVISIBLE);
                ParentDbName = "users";
            }
        });


    }



    private void LoginUser()
    {
        String phone = LoginPhone.getText().toString();
        String password = LoginPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "please write your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "please write your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("Please wait, while we are checking the credential");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            AllowAccessToAccount(phone,password);

        }
    }

    private void AllowAccessToAccount(String phone, String password) {

        if (checkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(ParentDbName).child(phone).exists())
                    {
                        Users usersData = snapshot.child(ParentDbName).child(phone).getValue(Users.class);

                        if (usersData.getPhone().equals(phone))
                        {
                            if (usersData.getPassword().equals(password))
                            {
                               if (ParentDbName == "users")
                               {
                                   Toast.makeText(LoginActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                                   LoadingBar.dismiss();
                                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                   Prevalent.CurrentOnlineUser = usersData;
                                   startActivity(intent);
                               }
                               else if (ParentDbName == "Admins")
                               {
                                   Toast.makeText(LoginActivity.this, "Logged in successfully as an Admin", Toast.LENGTH_SHORT).show();
                                   LoadingBar.dismiss();
                                   Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                   startActivity(intent);
                               }

                            }
                            else
                                {
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    LoadingBar.dismiss();
                                }

                        }
                    }
                else
                    {
                        Toast.makeText(LoginActivity.this, "This Account with this "+phone+ " do not exists", Toast.LENGTH_SHORT).show();
                        LoadingBar.dismiss();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}