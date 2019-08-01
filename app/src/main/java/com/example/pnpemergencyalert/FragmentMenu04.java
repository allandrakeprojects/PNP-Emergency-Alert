package com.example.pnpemergencyalert;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentMenu04 extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUploads;
    public String name;
    public String imageUrl;
    public String police_name;
    public String name_uid;
    public String datetime;
    public String lat;
    public String lng;
    public String police_uid;
    public String status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_04, container, false);
        init(view);
        return view;
    }

    private void init(View view){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Alerts");
        ref.addValueEventListener(
        new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("testtest", "detect");
                //Get map of users in datasnapshot
                for(com.google.firebase.database.DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    name_uid = singleSnapshot.getKey();
                    String name = singleSnapshot.child("name").getValue(String.class);
                    String datetime = singleSnapshot.child("datetime").getValue(String.class);
                    String lat = singleSnapshot.child("lat").getValue(String.class);
                    String lng = singleSnapshot.child("lng").getValue(String.class);
                    String police_uid = singleSnapshot.child("police_uid").getValue(String.class);
                    String police_name = singleSnapshot.child("police_name").getValue(String.class);
                    String status = singleSnapshot.child("status").getValue(String.class);
                    String image_url = singleSnapshot.child("image_url").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });



//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Alerts");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(com.google.firebase.database.DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                        String name_uid = singleSnapshot.getKey();
//                        String datetime = singleSnapshot.child("datetime").getValue(String.class);
//                        String lat = singleSnapshot.child("lat").getValue(String.class);
//                        String lng = singleSnapshot.child("lng").getValue(String.class);
//                        String police_uid = singleSnapshot.child("police_uid").getValue(String.class);
//                        asdasd = singleSnapshot.child("status").getValue(String.class);
//                        String asds = "";
//
//                        if (!police_uid.equals("-")) {
//                            DatabaseReference ref_police_uid = firebaseDatabase.getReference("Users/" + police_uid);
//                            ref_police_uid.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    Information information = dataSnapshot.getValue(Information.class);
//                                    textViewName.setText("Police Officer: " + information.getName());
//                                    final Context context = getContext();
//                                    Glide.with(context)
//                                            .load(information.getImageUrl())
//                                            .into(imageViewProfile);
//
//                                    name = information.getName();
//                                    imageUrl = information.getImageUrl();
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//
//                    Log.d("testtest", asdasd + " asdasdasdsd s1223");
//                try{
//                    // Get police officer if exists yet
//                    try {
////                        String police_uid = alerts.getPolice_uid();
////                        if (!police_uid.equals("-")) {
////                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + police_uid);
////                            databaseReference.addValueEventListener(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                    Information information = dataSnapshot.getValue(Information.class);
////                                    textViewName.setText("Police Officer: " + information.getName());
////                                    final Context context = getContext();
////                                    Glide.with(context)
////                                            .load(information.getImageUrl())
////                                            .into(imageViewProfile);
////
////                                    name = information.getName();
////                                    imageUrl = information.getImageUrl();
////                                }
////
////                                @Override
////                                public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                                }
////                            });
////                        } else {
////                            textViewName.setText("Police Officer: -");
////                            imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_black_48));
////                        }
//                    }catch (Exception err){
//
//                    }
//
//                } catch (Exception err){
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }
}
