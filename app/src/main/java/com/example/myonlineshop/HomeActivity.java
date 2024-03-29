package com.example.myonlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myonlineshop.admin.MaintainActivity;
import com.example.myonlineshop.model.Products;
import com.example.myonlineshop.prevelant.Prevalent;
import com.example.myonlineshop.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    public DatabaseReference productReference;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            type = bundle.get("Admin").toString();
//            type =  bundle.getString("admin");
            Log.e("admin", bundle.toString());


        }

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");


        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();


                if (!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_Navigation_Drawer, R.string.close_Navigation_Drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.activity_seller_nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);


        // Displaying UserName on Navigation Menu


        //        Prevalent  prevalent =

        View view = navigationView.getHeaderView(0);
        TextView textView = view.findViewById(R.id.user_profile_name);
        CircleImageView profileImagView = view.findViewById(R.id.uesr_profile_image);

        if (!type.equals("Admin")) {


            textView.setText(Prevalent.currentOnlineUser.getUser());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImagView);

        }


        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //        mAppBarConfiguration = new AppBarConfiguration.Builder(
        //                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
        //                .setDrawerLayout(drawer)
        //                .build();
        //
        //         NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReference.orderByChild("productState").equalTo("Approved"), Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {


                holder.txtProductPrice.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (type.equals("Admin")) {


                            Intent intent = new Intent(HomeActivity.this, MaintainActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        } else {


                            Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }


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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        Bundle bundle = new Bundle();
        if (id == R.id.nav_cart) {

            if(!type.equals("Admin"))
            {

                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);

            }

        } else if (id == R.id.nav_search) {

            if(!type.equals("Admin")){

                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);

            }



        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_settings) {

            if(!type.equals("Admin")){


                Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_logout) {

            if(!type.equals("Admin")){

                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }



        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}