package com.example.pnpemergencyalert;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class FragmentMenu01 extends Fragment {

    private ImageView imageViewAlert, imageViewProfile;
    private GifImageView gifAlert;
    private CardView cardViewInfo, cardViewAlert;
    private TextView textViewName, textViewStatus;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public static LatLng latLng;
    private static final String TAG = "testtesttest";
    Cursor cursorMsg, cursorDtl;
    String name;
    String imageUrl;
    public static String message;
    public static final int RequestPermissionCode  = 1 ;
    String activeUser;
    String color;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_01, container, false);

        //Firebase
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        init(view);
        return view;
    }


    private void init(View view){

        imageViewAlert = (ImageView)view.findViewById(R.id.imageViewAlert);
        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        gifAlert = (GifImageView)view.findViewById(R.id.gifAlert);
        cardViewInfo = (CardView) view.findViewById(R.id.cardViewInfo);
        cardViewAlert = (CardView) view.findViewById(R.id.cardViewAlert);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

        imageViewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_NEGATIVE:
                                cardViewAlert.setVisibility(View.INVISIBLE);
                                cardViewInfo.setVisibility(View.VISIBLE);

                                getLocationPermission();
                                getDeviceLocation();
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Confirm that you're in danger?").setPositiveButton("Cancel", dialogClickListener)
                        .setNegativeButton("Yes", dialogClickListener).show();
            }
        });

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Alerts/" + firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Alerts alerts = dataSnapshot.getValue(Alerts.class);
                try{
                    cardViewAlert.setVisibility(View.INVISIBLE);
                    cardViewInfo.setVisibility(View.VISIBLE);

                    if(alerts.getStatus().equals("P")){
                        textViewStatus.setText("Status: Waiting");
                    } else if(alerts.getStatus().equals("C")){
                        textViewStatus.setText("Status: On the way");
                        if(!alerts.getRead_ontheway()){
                            NotificationCompat.Builder b = new NotificationCompat.Builder(getActivity().getApplicationContext());
                            b.setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.drawable.baseline_account_circle_black_48)
                                    .setTicker("PNP Emergency Alert")
                                    .setContentTitle("PNP Emergency Alert")
                                    .setContentText("Our police officer is on the way now.");
                            NotificationManager nm = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify(1, b.build());

                            Alerts alerts_ = new Alerts(alerts.getPolice_uid(), alerts.getPolice_name(), alerts.getName(), alerts.getImageUrl(), alerts.getLat(), alerts.getLng(), alerts.getDatetime(), alerts.getStatus(), true);
                            databaseReference.setValue(alerts_);
                        }
                    } else if(alerts.getStatus().equals("D")){
                        textViewName.setText("Police Officer: -");
                        textViewStatus.setText("Status: -");
                        imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_black_48));
                        cardViewAlert.setVisibility(View.VISIBLE);
                        cardViewInfo.setVisibility(View.INVISIBLE);
                    }

                    // Get police officer if exists yet
                    try{
                        String police_uid = alerts.getPolice_uid();
                        if(!police_uid.equals("-")){
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + police_uid);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Information information = dataSnapshot.getValue(Information.class);
                                    textViewName.setText("Police Officer: " + information.getName());
                                    final Context context = getContext();
                                    Glide.with(context)
                                            .load(information.getImageUrl())
                                            .into(imageViewProfile);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else{
                            textViewName.setText("Police Officer: -");
                            imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_black_48));
                        }
                    } catch (Exception err){
                    }
                } catch (Exception err){
//                    textViewName.setText("Police Officer: -");
//                    textViewStatus.setText("Status: -");
//                    imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_black_48));
                    cardViewAlert.setVisibility(View.VISIBLE);
                    cardViewInfo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }












//    private void fetchLocation() {
//
//
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            Toast.makeText(getContext(), "dasdasdasdasdas1", Toast.LENGTH_SHORT).show();
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Required Location Permission")
//                        .setMessage("You have to give this permission to acess this feature")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .create()
//                        .show();
//
//
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            Toast.makeText(getContext(), "dasdasdasdasdas2", Toast.LENGTH_SHORT).show();
//            // Permission has already been granted
//            mFusedLocationClient.getLastLocation()
//            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    // Got last known location. In some rare situations this can be null.
//                    if (location != null) {
//                        // Logic to handle location object
//                        Double latitude = location.getLatitude();
//                        Double longitude = location.getLongitude();
//                        Log.d("testttestttest", "Latitude = "+latitude + "\nLongitude = " + longitude);
//
//                    } else{
//                        Log.d("testttestttest", "Latitude =");
//                    }
//                }
//            })
//            .addOnFailureListener(getActivity(), new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }








    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            Log.d("testtesttest", "Current location: http://www.google.com/maps/place/"+latLng.latitude+","+latLng.longitude);

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/" + user.getUid());
                            databaseReference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Date today = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String dateToStr = format.format(today);

                                    Information information = dataSnapshot.getValue(Information.class);
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Alerts alerts = new Alerts("-", "-", information.getName(), information.getImageUrl(), latLng.latitude + "", latLng.longitude + "", dateToStr, "P", false);
                                    String id = databaseReference.child("Alerts").push().getKey();
                                    databaseReference.child("Alerts").child(user.getUid()).setValue(alerts);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                ;
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: "+e.getMessage());
        }

    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            } else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }





























}