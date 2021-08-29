package com.example.ecommerce.Sellers;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String CategoryName,Description,Price,Pname, SaveCurrentDate,SaveCurrentTime ;
    private EditText InputProductName, InputProductDescription,InputProductPrice;
    private ImageView InputProductImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String ProductRandomKey,DownloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef,sellersRef;
    private ProgressDialog LoadingBar;

    private String sName, sAddress,sPhone,sEmail,sID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");


        Button addNewProduct = findViewById(R.id.btn_add_new_product);
        InputProductName = findViewById(R.id.ed_product_name);
        InputProductDescription = findViewById(R.id.ed_product_description);
        InputProductPrice = findViewById(R.id.ed_product_price);
        LoadingBar = new ProgressDialog(SellerAddNewProductActivity.this);

        InputProductImage= findViewById(R.id.iv_select_product_image);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery.launch("image/*");
            }
        });


        addNewProduct.setOnClickListener(v -> ValidateProductData());

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            sName = snapshot.child("name").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sID = snapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    ActivityResultLauncher<String> openGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        ImageUri = result;
                        InputProductImage.setImageURI(ImageUri);
                    }

                }
            }
    );



    private void   ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname =InputProductName.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image id mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        LoadingBar.setTitle("Adding New Product");
        LoadingBar.setMessage("Dear Seller Please wait, while we are adding the new product...");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        Calendar Calendar = java.util.Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(Calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = currentTime.format(Calendar.getTime());

        ProductRandomKey = SaveCurrentDate+SaveCurrentTime;


        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + ProductRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri) ;

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String messege = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + messege, Toast.LENGTH_SHORT).show();
                LoadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(SellerAddNewProductActivity.this, "Image uploaded successfuly", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        DownloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl() ;

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            DownloadImageUrl= task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Got the Product image Url successfully", Toast.LENGTH_SHORT).show();

                            SaveProductImageToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductImageToDatabase()
    {
        HashMap<String, Object> productMap =new HashMap<>();
        productMap.put("pid", ProductRandomKey);
        productMap.put("date", SaveCurrentDate);
        productMap.put("time", SaveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", DownloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        productMap.put("productState", "not approved");

        productMap.put("sellerName",sName );
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sellerEmail", sEmail);
        productMap.put("sID", sID);



        ProductRef.child(ProductRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            LoadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product id=s added successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            LoadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sellerInformation()
    {

    }


}



