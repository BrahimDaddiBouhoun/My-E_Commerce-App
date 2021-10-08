package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference pendingRef,completeRef;
    private String SaveCurrentDate,SaveCurrentTime,orderRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        pendingRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending");
        completeRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Complete");
        

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showOrders();

    }

    private void showOrders(){
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(pendingRef,AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AdminOrders model) {

                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount() + " $");
                        holder.userDataTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });
                            holder.confirmDelivery.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]
                                            {
                                                    "Yes",
                                                    "No"
                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                    builder.setTitle("Have you delivered the products to the Client");

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            if (i == 0) {
                                                Calendar Calendar = java.util.Calendar.getInstance();

                                                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                SaveCurrentDate = currentDate.format(Calendar.getTime());

                                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                                SaveCurrentTime = currentTime.format(Calendar.getTime());

                                                orderRandomKey = SaveCurrentDate+SaveCurrentTime;

                                                String uID = getRef(position).getKey();


                                                pendingRef.child(uID).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.child("client confirmation").getValue().toString().equals("ok"))
                                                        {
                                                            pendingRef.child(uID).child("admin confirmation").setValue("ok");
                                                            pendingRef.child(uID).child("delivery date").setValue(SaveCurrentDate);
                                                            pendingRef.child(uID).child("delivery time").setValue(SaveCurrentTime);
                                                            moveFireBaseNode(pendingRef.child(uID), completeRef.child(uID).child(orderRandomKey));
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(AdminNewOrdersActivity.this, "the client still has not confirm the good reception of the products", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            } else {
                                                finish();
                                            }

                                        }
                                    });
                                    builder.show();
                                }
                            });

                            holder.goToGoogleMap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uID = getRef(position).getKey();

                                    pendingRef.child(uID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                String lat = snapshot.child("lat").getValue().toString();
                                                String longu = snapshot.child("long").getValue().toString();

                                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+longu);
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                                    startActivity(mapIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
                                                                     int viewType){
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orders, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };


        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDataTime,userShippingAddress;
        public Button showOrdersBtn,goToGoogleMap,confirmDelivery;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDataTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);

            showOrdersBtn = itemView.findViewById(R.id.btn_show_all_product);
            goToGoogleMap = itemView.findViewById(R.id.google_map);
            confirmDelivery = itemView.findViewById(R.id.confirm_delivery);

        }
    }

    private void moveFireBaseNode(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");
                            fromPath.removeValue();

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void changeOrderState(String uID,String value)
    {
        completeRef.child(uID).child("state").setValue(value);
    }
}