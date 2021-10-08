package com.example.ecommerce.Buyers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ecommerce.R;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BuyerConfirmFinalOrderActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 99;
    private static final int REQUEST_CHECK_SETTINGS = 111;

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText,countryEditText,zipCodeEditText;
    private Button confirmOrderButton;

    private TextView Long, Lat, back;
    private ImageView gps;

    private int totalAmount;


    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationCallback locationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_confirm_final_order);

        confirmOrderButton = findViewById(R.id.btn_confirm_order);

        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone);
        addressEditText = findViewById(R.id.shipment_street_address);
        cityEditText = findViewById(R.id.shipment_city);
        countryEditText = findViewById(R.id.shipment_country);
        zipCodeEditText = findViewById(R.id.shipment_zip_code);

        Lat = findViewById(R.id.tv_lat);
        Long = findViewById(R.id.tv_long);
        gps = findViewById(R.id.gps);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
                .setFastestInterval(1000 * FAST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                    updateUIValues(locationResult.getLastLocation());

            }
        };

        totalAmount = getIntent().getIntExtra("totalAmount", 0);


        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getUserLocation();
            }
        });

    }

    private void getUserLocation() {
        LocationManager manager  = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        if(!(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))){


            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

            Task<LocationSettingsResponse> task =
                    LocationServices.getSettingsClient(BuyerConfirmFinalOrderActivity.this).checkLocationSettings(builder.build());

            task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            BuyerConfirmFinalOrderActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "GPS is already Enabled!", Toast.LENGTH_SHORT).show();
            updateGPS();
            startLocationUpdate();
        }
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
            updateGPS();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(getApplicationContext(),"User has clicked on OK - So GPS is on", Toast.LENGTH_SHORT).show();
                        updateGPS();
                        startLocationUpdate();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getApplicationContext(),"User has clicked on NO, THANKS - So GPS is still off.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
                case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    updateGPS();
                }
                else
                {
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS(){
        //get permission from the user to track GPS
        //get the current location from the user
        //update the UI

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BuyerConfirmFinalOrderActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateUIValues(location);
                    }
                }
            });
        }

    }

    private void updateUIValues(Location location) {

        Lat.setText(String.valueOf(location.getLatitude()));
        Long.setText(String.valueOf(location.getLongitude()));

        Geocoder geocoder = new Geocoder(BuyerConfirmFinalOrderActivity.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            addressEditText.setText(addresses.get(0).getAddressLine(0));
            cityEditText.setText(addresses.get(0).getSubLocality());
            countryEditText.setText(addresses.get(0).getCountryName());
            if (addresses.get(0).getPostalCode() != null) {
                zipCodeEditText.setText(addresses.get(0).getPostalCode());
            }
        }
        catch (Exception e){
            addressEditText.setText("unable to get street");
        }

    }

    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString())
        || TextUtils.isEmpty(phoneEditText.getText().toString())
        ||TextUtils.isEmpty(addressEditText.getText().toString())
        ||TextUtils.isEmpty(cityEditText.getText().toString())
        ||TextUtils.isEmpty(countryEditText.getText().toString())
        ||TextUtils.isEmpty(zipCodeEditText.getText().toString()))
        {
            Toast.makeText(this, "please provide all your info...", Toast.LENGTH_SHORT).show();
        }
        else
            {
              confirmOrder();
            }
    }

    private void confirmOrder()
    {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
        String saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("Pending")
                .child(Prevalent.CurrentOnlineUser.getPhone());

        HashMap<String,Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount",String.valueOf(totalAmount));
        ordersMap.put("name",nameEditText.getText().toString().trim());
        ordersMap.put("phone","0".concat(phoneEditText.getText().toString().trim()));
        ordersMap.put("address",addressEditText.getText().toString().trim());
        ordersMap.put("city",cityEditText.getText().toString().trim());
        ordersMap.put("lat",Lat.getText().toString().trim());
        ordersMap.put("long",Long.getText().toString().trim());
        ordersMap.put("conf date",saveCurrentDate);
        ordersMap.put("conf time",saveCurrentTime);
        ordersMap.put("client confirmation", "not ok");
        ordersMap.put("admin confirmation", "not ok");


        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    final DatabaseReference fromPath =FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.CurrentOnlineUser.getPhone())
                            .child("Products");
                    final DatabaseReference toPath =FirebaseDatabase.getInstance().getReference()
                            .child("Orders")
                            .child("Pending")
                            .child(Prevalent.CurrentOnlineUser.getPhone())
                            .child("Product");

                    moveFireBaseNode(fromPath,toPath);

                }
            }
        });

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
                            fromPath.removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(BuyerConfirmFinalOrderActivity.this, "your final order been placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(BuyerConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}