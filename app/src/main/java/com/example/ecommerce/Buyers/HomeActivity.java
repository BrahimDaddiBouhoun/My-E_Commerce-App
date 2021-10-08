package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerce.Buyers.newui.account.AccountFragment;
import com.example.ecommerce.Buyers.newui.cart.CartFragment;
import com.example.ecommerce.Buyers.newui.categories.CategoriesFragment;
import com.example.ecommerce.Buyers.newui.feed.FeedFragment;
import com.example.ecommerce.Buyers.newui.home.HomeFragment;
import com.example.ecommerce.Buyers.newui.productdetails.ProductDetailsFragment;
import com.example.ecommerce.Buyers.newui.search.SearchFragment;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityHomeBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout btnHome,btnCategories,btnFeed,btnAccount;
    private FloatingActionButton btnCart ;

    private ActivityHomeBinding binding;
    private String source = "";

    private ImageView home,category,feed,account;
    private TextView textHome,textCategory,textFeed,textAccount;

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

        home = binding.home;
        category = binding.category;
        feed = binding.feed;
        account = binding.account;

        textHome = binding.texthome;
        textCategory = binding.textcategory;
        textFeed = binding.textfeed;
        textAccount = binding.textaccount;


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_home)
        {
            changeHomeColor();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new HomeFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_categories)
        {
            changeCategoryColor();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new CategoriesFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_feed)
        {
            changeFeedColor();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new FeedFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_account)
        {
            changeAccountColor();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new AccountFragment())
                    .commit();
        }
        else if (v.getId()==R.id.btn_cart)
        {
            changeCartColor();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.buyer_fragment_host,new CartFragment())
                    .commit();
        }

    }

    @SuppressLint("ResourceAsColor")
    private void changeCartColor() {
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(category.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(feed.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(account.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(btnCart.getDrawable(), ContextCompat.getColor(this, R.color.orange_ff3));
        textHome.setTextColor(ContextCompat.getColor(this,R.color.black));
        textCategory.setTextColor(ContextCompat.getColor(this,R.color.black));
        textFeed.setTextColor(ContextCompat.getColor(this,R.color.black));
        textAccount.setTextColor(ContextCompat.getColor(this,R.color.black));
    }

    @SuppressLint("ResourceAsColor")
    private void changeAccountColor() {
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(category.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(feed.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(account.getDrawable(), ContextCompat.getColor(this, R.color.orange_ff3));
        DrawableCompat.setTint(btnCart.getDrawable(), ContextCompat.getColor(this, R.color.black));
        textHome.setTextColor(ContextCompat.getColor(this,R.color.black));
        textCategory.setTextColor(ContextCompat.getColor(this,R.color.black));
        textFeed.setTextColor(ContextCompat.getColor(this,R.color.black));
        textAccount.setTextColor(ContextCompat.getColor(this,R.color.orange_ff3));
    }

    @SuppressLint("ResourceAsColor")
    private void changeFeedColor() {
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(category.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(feed.getDrawable(), ContextCompat.getColor(this, R.color.orange_ff3));
        DrawableCompat.setTint(account.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(btnCart.getDrawable(), ContextCompat.getColor(this, R.color.black));
        textHome.setTextColor(ContextCompat.getColor(this,R.color.black));
        textCategory.setTextColor(ContextCompat.getColor(this,R.color.black));
        textFeed.setTextColor(ContextCompat.getColor(this,R.color.orange_ff3));
        textAccount.setTextColor(ContextCompat.getColor(this,R.color.black));
    }

    @SuppressLint("ResourceAsColor")
    private void changeCategoryColor() {
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(category.getDrawable(), ContextCompat.getColor(this, R.color.orange_ff3));
        DrawableCompat.setTint(feed.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(account.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(btnCart.getDrawable(), ContextCompat.getColor(this, R.color.black));
        textHome.setTextColor(ContextCompat.getColor(this,R.color.black));
        textCategory.setTextColor(ContextCompat.getColor(this,R.color.orange_ff3));
        textFeed.setTextColor(ContextCompat.getColor(this,R.color.black));
        textAccount.setTextColor(ContextCompat.getColor(this,R.color.black));
    }

    @SuppressLint("ResourceAsColor")
    private void changeHomeColor() {
        DrawableCompat.setTint(home.getDrawable(), ContextCompat.getColor(this, R.color.orange_ff3));
        DrawableCompat.setTint(category.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(feed.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(account.getDrawable(), ContextCompat.getColor(this, R.color.black));
        DrawableCompat.setTint(btnCart.getDrawable(), ContextCompat.getColor(this, R.color.black));
        textHome.setTextColor(ContextCompat.getColor(this,R.color.orange_ff3));
        textCategory.setTextColor(ContextCompat.getColor(this,R.color.black));
        textFeed.setTextColor(ContextCompat.getColor(this,R.color.black));
        textAccount.setTextColor(ContextCompat.getColor(this,R.color.black));
    }
}
