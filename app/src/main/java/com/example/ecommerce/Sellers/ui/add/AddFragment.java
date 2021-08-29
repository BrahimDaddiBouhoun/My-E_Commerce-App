package com.example.ecommerce.Sellers.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerce.Sellers.SellerAddNewProductActivity;
import com.example.ecommerce.databinding.FragmentSellerAddBinding;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private FragmentSellerAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSellerAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.ivTshirts.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","tShirts");
            startActivity(intent);
        });

        binding.ivSports.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","SportTShirts");
            startActivity(intent);
        });

        binding.ivFemaleDresses.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","FemaleDresses");
            startActivity(intent);
        });

        binding.ivSweather.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","SweatHers");
            startActivity(intent);
        });

        binding.ivGlasses.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","Glasses");
            startActivity(intent);
        });

        binding.ivHats.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","HatsCaps");
            startActivity(intent);
        });

        binding.ivPursesBags.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","WalletBagsPurses");
            startActivity(intent);
        });

        binding.ivShoess.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","Shoes");
            startActivity(intent);
        });

        binding.ivHeadphoness.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","HeadPhones");
            startActivity(intent);
        });

        binding.ivLaptops.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","Laptops");
            startActivity(intent);
        });

        binding.ivWatches.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","Watches");
            startActivity(intent);
        });

        binding.ivMobiles.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SellerAddNewProductActivity.class );
            intent.putExtra("category","MobilePhones");
            startActivity(intent);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}