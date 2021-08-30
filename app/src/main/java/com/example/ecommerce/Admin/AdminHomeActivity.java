package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;

public class AdminHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button logout = findViewById(R.id.btn_admin_logout);
        Button checkOrders = findViewById(R.id.btn_check_orders);
        Button disapproveProductBtn = findViewById(R.id.btn_disapprove);
        Button approveProductBtn = findViewById(R.id.btn_approve_products);

                logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class );
                startActivity(intent);
            }
        });

        disapproveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminDisapproveProductsActivity.class );
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        approveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminApproveProductActivity.class );
                startActivity(intent);
            }
        });
    }
}