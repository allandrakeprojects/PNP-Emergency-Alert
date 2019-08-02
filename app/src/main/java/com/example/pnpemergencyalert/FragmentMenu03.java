package com.example.pnpemergencyalert;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class FragmentMenu03 extends Fragment {

    public ProgressDialog progressDialog;
    public LinearLayout helpCitizen, helpAdmin, helpPoliceOfficer;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_03, container, false);
        init(view);
        return view;


    }

    public void init(View view){
        helpCitizen = (LinearLayout)view.findViewById(R.id.helpCitizen);
        helpAdmin = (LinearLayout)view.findViewById(R.id.helpAdmin);
        helpPoliceOfficer = (LinearLayout)view.findViewById(R.id.helpPoliceOfficer);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting info...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Information information = dataSnapshot.getValue(Information.class);
                if(information.getType().equals("C")){
                    helpCitizen.setVisibility(View.VISIBLE);
                    helpAdmin.setVisibility(View.GONE);
                    helpPoliceOfficer.setVisibility(View.GONE);
                } else if(information.getType().equals("A")){
                    helpCitizen.setVisibility(View.GONE);
                    helpAdmin.setVisibility(View.VISIBLE);
                    helpPoliceOfficer.setVisibility(View.GONE);
                } else if(information.getType().equals("P")){
                    helpCitizen.setVisibility(View.GONE);
                    helpAdmin.setVisibility(View.GONE);
                    helpPoliceOfficer.setVisibility(View.VISIBLE);
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
}
