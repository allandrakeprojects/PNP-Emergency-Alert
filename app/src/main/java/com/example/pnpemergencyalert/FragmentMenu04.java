package com.example.pnpemergencyalert;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentMenu04 extends Fragment {

    List<Alerts> alertsList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_04, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        alertsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Alerts");
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(com.google.firebase.database.DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String name_uid = singleSnapshot.getKey();
                            String name = singleSnapshot.child("name").getValue(String.class);
                            String datetime = singleSnapshot.child("datetime").getValue(String.class);
                            String lat = singleSnapshot.child("lat").getValue(String.class);
                            String lng = singleSnapshot.child("lng").getValue(String.class);
                            String police_uid = singleSnapshot.child("police_uid").getValue(String.class);
                            String police_name = singleSnapshot.child("police_name").getValue(String.class);
                            String status = singleSnapshot.child("status").getValue(String.class);
                            String imageUrl = singleSnapshot.child("image_url").getValue(String.class);
                            if(status.equals("P")){
                                status = "Waiting";
                            } else if(status.equals("C")){
                                status = "On the way";
                            }

                            Log.d("test", police_uid);
                            Log.d("test", police_name);
                            Log.d("test", name);
                            Log.d("test", imageUrl);
                            Log.d("test", lat);
                            Log.d("test", lng);
                            Log.d("test", datetime);
                            Log.d("test", status);
                            Log.d("test", "-----------");

                            alertsList.add(
                                    new Alerts(
                                            police_uid,
                                            police_name,
                                            name,
                                            imageUrl,
                                            lat,
                                            lng,
                                            datetime,
                                            status,
                                            false));
                        }

                        //creating recyclerview adapter
                        final Context context = getContext();
                        AlertsAdapter alertsAdapter = new AlertsAdapter(getActivity().getApplicationContext(), alertsList);

                        //setting adapter to recyclerview
                        recyclerView.setAdapter(alertsAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


















        init(view);
        return view;
    }

    private void init(View view){





    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }
}
