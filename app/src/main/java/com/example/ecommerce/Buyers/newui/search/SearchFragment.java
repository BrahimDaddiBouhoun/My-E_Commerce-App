package com.example.ecommerce.Buyers.newui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Buyers.newui.home.HomeFragment;
import com.example.ecommerce.Buyers.newui.productdetails.ProductDetailsFragment;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.databinding.BuyerSearchFragmentBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class SearchFragment extends Fragment {

    private BuyerSearchFragmentBinding binding;

    private String searchIt = "" ;
    private EditText search ;

    private ImageView back ;

    private RecyclerView searchList;
    GridLayoutManager gridLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BuyerSearchFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        back = binding.backSearch;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                HomeFragment frag = new HomeFragment();
                fragmentTransaction.replace(R.id.buyer_fragment_host,frag).commit();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            searchIt = bundle.getString("search");
        }


        searchList = binding.searchRv;
        searchList.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        searchList.setLayoutManager(gridLayoutManager);

        if (searchIt != "")
        {
            performSearch(searchIt);
        }

        search = binding.search;
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchIt = search.getText().toString().trim();

                    performSearch(searchIt);

                    return true;
                }
                return false;
            }
        });


        return root ;
    }

    private void performSearch(String searchIt) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname").startAt(searchIt).endAt(searchIt + "uf8ff"), Products.class)
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
                                Bundle bundle = new Bundle();
                                bundle.putString("pid", model.getPid());

                                ProductDetailsFragment fragment2 = new ProductDetailsFragment();
                                fragment2.setArguments(bundle);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.buyer_fragment_host, fragment2).addToBackStack(null)
                                        .commit();
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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}