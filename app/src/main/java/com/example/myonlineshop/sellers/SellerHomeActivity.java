package com.example.myonlineshop.sellers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myonlineshop.MainActivity;
import com.example.myonlineshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class SellerHomeActivity extends AppCompatActivity {

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


    }


}