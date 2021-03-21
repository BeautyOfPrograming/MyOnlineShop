package com.example.myonlineshop.admin;

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

import com.example.myonlineshop.R;
import com.example.myonlineshop.model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {


    private RecyclerView orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this
        ));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.orders_layout), parent, false);

                return new AdminOrdersViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.userName.setText("Your name : " + model.getName());
                holder.userPhone.setText("Your phone : " + model.getPhone());
                holder.userShipping.setText("Your state : " + model.getAddress());
                holder.userDateTime.setText("Your name : " + model.getDate() + model.getTime());
                holder.userTotalPrice.setText("Your TotalPrice : " + model.getTotalamount() + ", " + model.getCity());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence option[] = new CharSequence[]{

                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped the item already");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (i == 0) {


                                   String userId =getRef(position).getKey();
                                    removeOrder(userId);
                                } else {

                                    finish();
                                }
                            }
                        });
                        builder.show();

                    }
                });


                holder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userID = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductActivity.class);
                        intent.putExtra("uid", userID);
                        startActivity(intent);
                    }
                });

            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhone, userTotalPrice, userDateTime, userShipping;

        public Button showOrderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {


            super(itemView);


            userName = itemView.findViewById(R.id.order_username);
            userPhone = itemView.findViewById(R.id.order_phone);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShipping = itemView.findViewById(R.id.order_address_city);
            showOrderBtn = itemView.findViewById(R.id.show_all_products);

        }


    }

    private void removeOrder(String userId) {

        ordersRef.child(userId).removeValue();

    }
}