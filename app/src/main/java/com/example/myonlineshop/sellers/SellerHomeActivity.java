package com.example.myonlineshop.sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myonlineshop.MainActivity;
import com.example.myonlineshop.R;
import com.example.myonlineshop.admin.AdminCheckNewProductsActivity;
import com.example.myonlineshop.model.Products;
import com.example.myonlineshop.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Log.d("inside_seller_activity", "I am runned");


            switch (item.getItemId()) {

                case R.id.navigation_logout:

                    Log.d("inside_seller_activity", "I am runned");
                    Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);


                    final FirebaseAuth auth;
                    auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    Toast.makeText(SellerHomeActivity.this, "Inside seller home activity", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;


                case R.id.navigation_add:
                    Intent intentaddproduct = new Intent(SellerHomeActivity.this, SellerAddNewProductActivity.class);
                    startActivity(intentaddproduct);
                    return true;


            }


            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.activity_seller_nav_view);
        navView.setOnNavigationItemSelectedListener(listener);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.seller_nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);


        recyclerView = findViewById(R.id.seller_home_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> unverfiedProducts = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(unverfiedProducts) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {


                holder.txtProductPrice.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String id = model.getPid();

                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"

                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductsActivity.this);

                        builder.setTitle("Do you want to approve this product, Are you sure");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (i == 0) {

                                    changeProduct(id);

                                } else if (i == 1) {


                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                ProductViewHolder holder = new ProductViewHolder(view);


                return holder;
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
}