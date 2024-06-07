package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        Intent incomingIntent = getIntent();
        if (incomingIntent!=null){
            String ifFirstTime = incomingIntent.getStringExtra("IfFirstTime");
            if (ifFirstTime=="Yes"){
                Toast.makeText(this, "Welcome to Courtify", Toast.LENGTH_SHORT).show();
            }
        }
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.nav_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new MapFragment()).commit();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new MapFragment();
                        break;

                    case R.id.nav_courts:
                        fragment = new Courts_Task();
                        break;

                    case R.id.nav_people:
                        fragment = new Peoples_Task();
                        break;

                    case R.id.nav_friends:
                        fragment = new FriendsFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new Profiles_Task();
                        break;
                    default:
                        fragment = new MapFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
                            != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request them
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Permissions are already granted
                // You can start using location-related features or Internet-related functionality here
            }
        } else {
            // For devices running on Android versions lower than Marshmallow,
            // permissions are granted at installation time, so nothing to do here
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions are granted
                // You can start using location-related features or Internet-related functionality here
            } else {
                // Some or all permissions are denied.
                // Handle this situation, show explanations, or disable functionality
            }
        }
    }

}