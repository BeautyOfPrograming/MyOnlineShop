package com.example.myonlineshop.sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.activity_seller_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();


        BottomNavigationView.OnNavigationItemReselectedListener listener =  new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){



//                    case R.id.navigation_add:
//                        final FirebaseAuth auth;
//                        auth = FirebaseAuth.getInstance();
//                        auth.signOut();
//
//                    case R.id.navigation_add:
//                        final FirebaseAuth auth;
//                        auth = FirebaseAuth.getInstance();
//                        auth.signOut();


                    case R.id.navigation_add:
                        final FirebaseAuth auth;
                        auth = FirebaseAuth.getInstance();
                        auth.signOut();

                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                }
            }
        };

        NavController navController = Navigation.findNavController(this, R.id.seller_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}