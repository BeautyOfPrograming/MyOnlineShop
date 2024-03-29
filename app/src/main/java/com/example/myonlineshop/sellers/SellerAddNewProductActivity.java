package com.example.myonlineshop.sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.myonlineshop.R;
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

    private static final int GALLERY_CODE = 1;
    private Uri imageUri;
    private String category, saveCurrentlyDate, saveCurrentlyTime;

    private String description, name, price;

    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private String productUniqueKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef, sellerRef;
    private String sellername, selleraddress, selleremail, sellersid,sellerphone;

    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_add_new_product);

        category = getIntent().getExtras().get("category").toString();

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        loading = new ProgressDialog(SellerAddNewProductActivity.this);

        AddNewProductButton = findViewById(R.id.add_new_product);
        InputProductImage = findViewById(R.id.select_product_image);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SellerAddNewProductActivity.this, "get image", Toast.LENGTH_SHORT).show();
                insertGalleryImage();
            }
        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProduct();

            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {


                            sellername = snapshot.child("name").getValue().toString();
                            selleraddress = snapshot.child("address").getValue().toString();
                            selleremail = snapshot.child("email").getValue().toString();
                            sellersid = snapshot.child("sid").getValue().toString();
                            sellerphone = snapshot.child("phone").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        Toast.makeText(this, "welcome admin" + category, Toast.LENGTH_SHORT).show();


    }

    private void ValidateProduct() {


        description = InputProductDescription.getText().toString();
        name = InputProductName.getText().toString();
        price = InputProductPrice.getText().toString();

        if (imageUri == null) {

            Toast.makeText(this, "please select image ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(description)) {

            Toast.makeText(this, "please enter product description ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(name)) {

            Toast.makeText(this, "please enter  product name ", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(price)) {

            Toast.makeText(this, "please enter product price ", Toast.LENGTH_SHORT).show();

        } else {


            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {


        loading.setTitle("Adding new Product");
        loading.setMessage("Dear Seller, please wait while we are adding new product...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentlyDateFormat = new SimpleDateFormat("MM:dd:yy");
        saveCurrentlyDate = currentlyDateFormat.format(calendar.getTime());


        SimpleDateFormat currentlyTimeFormat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentlyTime = currentlyTimeFormat.format(calendar.getTime());


        productUniqueKey = saveCurrentlyDate + saveCurrentlyTime;

        StorageReference filepath = ProductImagesRef.child(imageUri.getLastPathSegment() + productUniqueKey + "jpg");

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(SellerAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(SellerAddNewProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "getting product image Uri successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();

                        }

                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productUniqueKey);
        productMap.put("date", saveCurrentlyDate);
        productMap.put("time", saveCurrentlyTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", category);
        productMap.put("price", price);
        productMap.put("pname", name);
        productMap.put("sellerName", sellername);
        productMap.put("sellerEmail", selleremail);
        productMap.put("sellerPhone", sellerphone);
        productMap.put("sellerAddress", selleraddress);
        productMap.put("sellerSid", sellersid);

        productMap.put("productState", "Not Approved");

        ProductsRef.child(productUniqueKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerProductActivity.class);
                            startActivity(intent);


                            loading.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();


                        } else {

                            loading.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void insertGalleryImage() {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();

            InputProductImage.setImageURI(imageUri);

        }
    }
}