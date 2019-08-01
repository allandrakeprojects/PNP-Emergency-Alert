package com.example.pnpemergencyalert;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentMenu04 extends Fragment {

    List<Alerts> alertsList;
    RecyclerView recyclerView;
    TextView textViewNoDataYet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_04, container, false);

        textViewNoDataYet = (TextView)view.findViewById(R.id.textViewNoDataYet);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        alertsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Alerts");
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //creating recyclerview adapter
                        final Context context = getContext();
                        AlertsAdapter alertsAdapter = new AlertsAdapter(context, alertsList);
                        alertsList.clear();
                        alertsAdapter.notifyDataSetChanged();
                        int count = 0;
                        for(com.google.firebase.database.DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String p_status = singleSnapshot.child("p_status").getValue(String.class);
                            if(!p_status.equals("D")){
                                count++;
                                String c_datecreated = singleSnapshot.child("c_datecreated").getValue(String.class);
                                String c_imgUrl = singleSnapshot.child("c_imgUrl").getValue(String.class);
                                String c_lat = singleSnapshot.child("c_lat").getValue(String.class);
                                String c_lng = singleSnapshot.child("c_lng").getValue(String.class);
                                String c_name = singleSnapshot.child("c_name").getValue(String.class);
                                Boolean c_read = singleSnapshot.child("c_read").getValue(Boolean.class);
                                String c_uid = singleSnapshot.child("c_uid").getValue(String.class);
                                String p_name = singleSnapshot.child("p_name").getValue(String.class);
                                String p_uid = singleSnapshot.child("p_uid").getValue(String.class);
                                if(p_status.equals("W")){
                                    p_status = "Waiting";
                                } else if(p_status.equals("O")){
                                    p_status = "On the way";
                                } else if(p_status.equals("D")){
                                    p_status = "Done";
                                }

                                alertsList.add(
                                    new Alerts(
                                        c_uid,
                                        c_name,
                                        c_imgUrl,
                                        c_lat,
                                        c_lng,
                                        c_datecreated,
                                        p_uid,
                                        p_name,
                                        p_status,
                                        false));
                            }
                        }

                        if(count == 0){
                            textViewNoDataYet.setVisibility(View.VISIBLE);
                        } else{
                            textViewNoDataYet.setVisibility(View.GONE);
                        }


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
