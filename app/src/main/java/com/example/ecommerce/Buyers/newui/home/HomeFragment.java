package com.example.ecommerce.Buyers.newui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminDisapproveProductsActivity;
import com.example.ecommerce.Buyers.newui.productdetails.ProductDetailsFragment;
import com.example.ecommerce.Buyers.newui.search.SearchFragment;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.databinding.BuyerHomeFragmentBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private DatabaseReference ProductsRef;

    private RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    private String type = "";
    private String searchInput;
    
    private BuyerHomeFragmentBinding binding;

    private EditText search ;

    private ImageView profileImage;

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BuyerHomeFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.shopNowRv;
        recyclerView.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        search = binding.search;
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInput = search.getText().toString().trim();

                    if (searchInput != null) {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                                .beginTransaction();

                        Bundle bundle = new Bundle();
                        bundle.putString("search", searchInput);

                        SearchFragment frag = new SearchFragment();
                        frag.setArguments(bundle);

                        fragmentTransaction.replace(R.id.buyer_fragment_host,frag).commit();
                    }

                    return true;
                }
                return false;
            }
        });

        profileImage = binding.homeProfileImage;
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Prevalent.CurrentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("approved"),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setVisibility(View.GONE);
                        holder.txtProductPrice.setText(model.getPrice() + " $");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminDisapproveProductsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("pid", model.getPid());




                                    ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                    fragment2.setArguments(bundle);

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.buyer_fragment_host, fragment2).addToBackStack(null)
                                            .commit();

                                }


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    
}