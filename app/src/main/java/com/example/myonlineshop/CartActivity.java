package com.example.myonlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myonlineshop.model.Cart;
import com.example.myonlineshop.prevelant.Prevalent;
import com.example.myonlineshop.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button next;
    private TextView totaPrice, txtMsg;
    private int totalPriceInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        totaPrice = findViewById(R.id.totalprice);

        next = findViewById(R.id.next_process_btn);
        txtMsg = findViewById(R.id.congrulation_message);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totaPrice.setText(String.valueOf(totalPriceInt));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("totalprice", String.valueOf(totalPriceInt));
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

        DatabaseReference getFIle = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(getFIle.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class
                ).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText(" Price = " + model.getPrice() + "$");
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());


                int onetypeProduct = ((Integer.valueOf(model.getPrice()))) * ((Integer.valueOf(model.getQuantity())));
                totalPriceInt = totalPriceInt + onetypeProduct;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[] = new CharSequence[]{

                                "Edit",
                                "Remove"

                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options :");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (i == 0) {

                                    Intent intent = new Intent(CartActivity.this, DetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } else if (i == 1) {

                                    getFIle.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(CartActivity.this, "The Product is removed successfuly from cart", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler_item, parent, false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);

                return cartViewHolder;
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();


    }


    private void checkOrderState() {

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {


                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        Toast.makeText(CartActivity.this, "Your product  is shipped successfully", Toast.LENGTH_SHORT).show();

                        recyclerView.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                    } else if (shippingState.equals("not shipped")) {


                        Toast.makeText(CartActivity.this, "Shipping state  == Not Shipped", Toast.LENGTH_SHORT).show();

                        totaPrice.setText("Shipping state  == Not Shipped");

                        txtMsg.setText("Congratulation your order has been accepted and you item will be at your door step");

                        recyclerView.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more Products when your oder is finalized", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}