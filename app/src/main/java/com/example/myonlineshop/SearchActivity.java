package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myonlineshop.model.Products;
import com.example.myonlineshop.viewHolder.productViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {


    private Button searchBtn;
    private EditText searchBox;
    private RecyclerView searchList;
    private String searchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchBtn = findViewById(R.id.searchBtn);
        searchBox = findViewById(R.id.searchBox);

        searchList = findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


    }

    @Override
    protected void onStart() {
        super.onStart();



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchInput = searchBox.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

                FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ref.orderByChild("pname").startAt(searchInput), Products.class)
                        .build();


                FirebaseRecyclerAdapter<Products, productViewHolder> adapter = new FirebaseRecyclerAdapter<Products, productViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull Products model) {


                        holder.txtProductPrice.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                        productViewHolder holder = new productViewHolder(view);


                        return holder;
                    }
                };

                searchList.setAdapter(adapter);
                adapter.startListening();

            }
        });



    }
}