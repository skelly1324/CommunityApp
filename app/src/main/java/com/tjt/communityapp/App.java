package com.tjt.communityapp;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Ryan on 4/21/2018.
 */

public class App extends Application {
    //The singleton
    public static App s;
    public Activity currentActivity;

    //The current user
    public User currentUser;

    //References to our other singletons
    public FirebaseAuth fAuth;
    public DatabaseReference fDatabase;
    public GeoFire geoFire;
    public FusedLocationProviderClient gLocationClient;

    //Request codes so we know what proccess is being handled in onRequestPermissionsResult
    private final int LOCATION_PERMISION_REQUEST_CODE = 1;

    //A progressDialog we use when loading or requesting data, in progress()
    protected ProgressDialog progressDialog;

    //Shared Data for MapFragment
    Marker tempMarker;
    LatLng newPinCoords;

    @Override
    public void onCreate(){
        super.onCreate();
        s = this;

        //Setup singletons
        fDatabase = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        gLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geoFire = new GeoFire(fDatabase.child("geofire"));
    }

    //Self-explanitory and simple functions for affordance
    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void snackbar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG ).show();
    }
    public void dialog(DialogFragment dialog, Activity activity){
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        dialog.show(ft, "dialog");
    }
    public void progress(String message, Activity context){
        if(progressDialog != null){
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    public void dismissProgress(){
        progressDialog.dismiss();
    }

    //Change the activity
    public void setActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    //Logout
    public void logout(){
        fAuth.signOut();

        FragmentManager fragmentManager = currentActivity.getFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
