package com.example.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    Button bt_location;
    TextView adresse, localite,atitude , longtitude,pays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_location = findViewById(R.id.btn_location);
        adresse = findViewById(R.id.adresse);
        localite = findViewById(R.id.localite);
        atitude = findViewById(R.id.atitude);
        longtitude = findViewById(R.id.longtitude);
        pays = findViewById(R.id.pays);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);


        bt_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLocation();
            }
        });

    }
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        } else {
//R??cup??ration de la derni??re localisation
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
//initialiser geocoder
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//initialiser l???adresse de localisation
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
//afficher la latitude dans le textiew
                            atitude.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Latitude : </b><br></font>"
                                            + addresses.get(0).getLatitude()
                            ));

// afficher la longitude dans le textview
                            longtitude.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Longitude : </b><br></font>"
                                            + addresses.get(0).getLongitude()
                            ));
// afficher le pays dans le textview
                            pays.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Nom de pays : </b><br></font>"
                                            + addresses.get(0).getCountryName()
                            ));
// afficher la localit?? dans le textview
                            localite.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Localit?? : </b><br></font>"
                                            + addresses.get(0).getLocality()
                            ));
// afficher l???adresse dans le textview
                            adresse.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Adresse : </b><br></font>"
                                            + addresses.get(0).getAddressLine(0)
                            ));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Aucune position enregistr??e", Toast.LENGTH_SHORT).show();}
                }
            });
        }
        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }});
}}