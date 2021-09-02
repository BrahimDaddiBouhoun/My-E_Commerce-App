package com.example.ecommerce.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ecommerce.Buyers.newui.account.AccountFragment;
import com.example.ecommerce.Buyers.newui.cart.CartFragment;
import com.example.ecommerce.Buyers.newui.categories.CategoriesFragment;
import com.example.ecommerce.Buyers.newui.feed.FeedFragment;
import com.example.ecommerce.Buyers.newui.home.HomeFragment;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout btnHome,btnCategories,btnFeed,btnAccount;
    private FloatingActionButton btnCart ;

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.buyer_fragment_host,new HomeFragment())
                .commit();

        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(this);

        btnCategories = findViewById(R.id.btn_categories);
        btnCategories.setOnClickListener(this);

        btnFeed = findViewById(R.id.btn_feed);
        btnFeed.setOnClickListener(this);

        btnAccount = findViewById(R.id.btn_account);
        btnAccount.setOnClickListener(this);

        btnCart = findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(this);

//        btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this,BuyerCartActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_home)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new HomeFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_categories)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new CategoriesFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_feed)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new FeedFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_account)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new AccountFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_cart)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new CartFragment())
                    .commit();
        }
    }
}
