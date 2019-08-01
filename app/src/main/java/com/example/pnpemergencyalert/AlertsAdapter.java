package com.example.pnpemergencyalert;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Alerts> alertsList;

    public View view;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    //getting the context and product list with constructor
    public AlertsAdapter(Context mCtx, List<Alerts> alertsList) {
        this.mCtx = mCtx;
        this.alertsList = alertsList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.activity_list_alerts,null);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final Alerts alerts = alertsList.get(position);

        //binding the data with the viewholder views
//        holder.textViewInfoList.setText(alerts.getName() + "\n" + alerts.getDatetime() + "\nPolice Officer: " + alerts.getPolice_name() + "\nStatus: " + alerts.getStatus());
//        holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "http://www.google.com/maps/place/"+alerts.getLat()+","+alerts.getLng();
//                try {
//                    Uri uri = Uri.parse("googlechrome://navigate?url="+ url);
//                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    view.getContext().startActivity(i);
//                } catch (ActivityNotFoundException e) {
//                    Uri uri = Uri.parse(url);
//                    // Chrome is probably not installed
//                    // OR not selected as default browser OR if no Browser is selected as default browser
//                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    view.getContext().startActivity(i);
//                }
//            }
//        });
//        Glide.with(holder.imageViewProfileList.getContext())
//            .load(alerts.getImageUrl())
//            .into(holder.imageViewProfileList);
//
//        if(alerts.getStatus().equals("Waiting")){
//            holder.buttonRespond.setVisibility(View.VISIBLE);
//            holder.buttonDone.setVisibility(View.GONE);
//        } else if(alerts.getStatus().equals("On the way")){
//            holder.buttonRespond.setVisibility(View.GONE);
//            holder.buttonDone.setVisibility(View.VISIBLE);
//        }

        holder.buttonRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Alerts/" + alerts.getC_uid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Alerts alerts = dataSnapshot.getValue(Alerts.class);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            final DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/" + user.getUid());

                            databaseReference1.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Information information = dataSnapshot.getValue(Information.class);
                                                try{
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                                                    Alerts alerts_ = new Alerts(alerts.getName_uid(), user.getUid(), information.getName(), alerts.getName(), alerts.getImageUrl(), alerts.getLat(), alerts.getLng(), alerts.getDatetime(), "C", true);
//                                                    databaseReference.setValue(alerts_);
                                                } catch (Exception err){

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                );


                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Alerts/" + alerts.getC_uid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name_uid = dataSnapshot.getKey().toString();
                        final Alerts alerts = dataSnapshot.getValue(Alerts.class);
//                        Alerts alerts_ = new Alerts(alerts.getName_uid(), alerts.getPolice_uid(), alerts.getPolice_name(), alerts.getName(), alerts.getImageUrl(), alerts.getLat(), alerts.getLng(), alerts.getDatetime(), "D", true);
//                        databaseReference.setValue(null);


                        final DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference("History/" + name_uid);
                        databaseReference12.setValue(alerts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return alertsList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInfoList;
        ImageView imageViewProfileList;
        Button buttonViewMap, buttonRespond, buttonDone;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewInfoList = itemView.findViewById(R.id.textViewInfoList);
            imageViewProfileList = itemView.findViewById(R.id.imageViewProfileList);
            buttonViewMap = itemView.findViewById(R.id.buttonViewMap);
            buttonRespond = itemView.findViewById(R.id.buttonRespond);
            buttonDone = itemView.findViewById(R.id.buttonDone);
        }
    }
}