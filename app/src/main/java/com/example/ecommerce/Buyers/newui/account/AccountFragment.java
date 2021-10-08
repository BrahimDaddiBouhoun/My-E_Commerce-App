package com.example.ecommerce.Buyers.newui.account;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerce.Buyers.BuyerEditProfileActivity;
import com.example.ecommerce.Buyers.BuyerOrderStateActivity;
import com.example.ecommerce.LoginActivity;
import com.example.ecommerce.databinding.BuyerAccountFragmentBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    private BuyerAccountFragmentBinding binding ;
    private TextView editProfile,logout,userName,phoneNumber,userAddress;
    private ImageView profileImage;
    private LinearLayout orderState;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BuyerAccountFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileImage = binding.settingProfileIv;
        userName = binding.username;
        phoneNumber = binding.phone;
        userAddress = binding.address;

        userInfoDisplay(profileImage,userName, phoneNumber,userAddress);

        editProfile = binding.editProfile;
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyerEditProfileActivity.class);
                startActivity(intent);
            }
        });

        logout = binding.accountLogout;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        orderState= binding.lOrderState;
        orderState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyerOrderStateActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void userInfoDisplay(ImageView profileImage, TextView userName, TextView phoneNumber, TextView userAddress) {

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Prevalent.CurrentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if(snapshot.exists())
                    {
                        if (snapshot.child("image").exists())
                        {
                            String image = snapshot.child("image").getValue().toString();
                            Picasso.get().load(image).into(profileImage);
                        }

                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();


                        userName.setText(name);
                        phoneNumber.setText(phone);
                        userAddress.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}