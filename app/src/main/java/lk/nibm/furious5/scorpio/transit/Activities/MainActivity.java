package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import lk.nibm.furious5.scorpio.transit.Fragments.QrCodeFragment;
import lk.nibm.furious5.scorpio.transit.Fragments.HomeFragment;
import lk.nibm.furious5.scorpio.transit.Fragments.PeoplesFragment;
import lk.nibm.furious5.scorpio.transit.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;

    private Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottomNav);

        HomeFragment homeFragment = new HomeFragment();
        Fragment qrCodeFragment = new QrCodeFragment();
        Fragment peoplesFragment = new PeoplesFragment();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, homeFragment).commit();

        String token = getIntent().getStringExtra("Token");
        showToast(token);
        getCurrentLocation();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.home) {
                    selectedFragment = HomeFragment.newInstance(token);
                } else if (item.getItemId() == R.id.qr) {
                    selectedFragment = QrCodeFragment.newInstance(lat, lon, token);
                } else if (item.getItemId() == R.id.friend) {
                    selectedFragment = peoplesFragment;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, selectedFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void showToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null)
                            {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses != null && addresses.size() > 0) {
//                                        showToast("Lat: " + addresses.get(0).getLatitude());
//                                        showToast("Long: " + addresses.get(0).getLongitude());
                                        lat = addresses.get(0).getLatitude();
                                        lon = addresses.get(0).getLongitude();
                                    } else {
                                        showToast("Unable to get location");
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                    });
        }else {
            askPermission();
        }
    }

    private void askPermission()
    {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}