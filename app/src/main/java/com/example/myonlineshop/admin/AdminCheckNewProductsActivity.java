package com.example.myonlineshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myonlineshop.Interface.ItemClickListener;
import com.example.myonlineshop.R;
import com.example.myonlineshop.model.Products;
import com.example.myonlineshop.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_products);

        recyclerView = findViewById(R.id.admin_approve_new_products);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> unverfiedProducts = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductsRef.orderByChild("Products").equalTo("Not Approved"), Products.class)
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

                                    changeProductState(id);

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

    private void changeProductState(String id) {

        unverifiedProductsRef.child(id).child("productState").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(AdminCheckNewProductsActivity.this, "The product has been approved and now is available for sale", Toast.LENGTH_SHORT).show();

                    }
                });
    }


}