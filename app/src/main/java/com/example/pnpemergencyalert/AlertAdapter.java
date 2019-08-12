package com.example.pnpemergencyalert;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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


public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Alert> alertList;

    public View view;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    //getting the context and product list with constructor
    public AlertAdapter(Context mCtx, List<Alert> alertList) {
        this.mCtx = mCtx;
        this.alertList = alertList;
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
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final Alert alert = alertList.get(position);

        //binding the data with the viewholder views
        holder.textViewInfoList.setText(alert.getC_name() + "\n" + alert.getC_datecreated() + "\nPolice Officer: " + alert.getP_name() + "\nStatus: " + alert.getP_status());
        holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.google.com/maps/place/"+ alert.getC_lat()+","+ alert.getC_lng();
                try {
                    Uri uri = Uri.parse("googlechrome://navigate?url="+ url);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse(url);
                    // Chrome is probably not installed
                    // OR not selected as default browser OR if no Browser is selected as default browser
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(i);
                }
            }
        });
        Glide.with(holder.imageViewProfileList.getContext())
            .load(alert.getC_imgUrl())
            .into(holder.imageViewProfileList);

        if(alert.getP_status().equals("Waiting")){
            holder.buttonRespond.setVisibility(View.VISIBLE);
            holder.buttonDone.setVisibility(View.GONE);
        } else if(alert.getP_status().equals("On the way")){
            holder.buttonRespond.setVisibility(View.GONE);
            holder.buttonDone.setVisibility(View.VISIBLE);
        } else if(alert.getP_status().equals("Done")){
            holder.buttonRespond.setVisibility(View.GONE);
            holder.buttonDone.setVisibility(View.GONE);
        }

        holder.buttonRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                final DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/" + user.getUid());
                databaseReference1.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Information information = dataSnapshot.getValue(Information.class);
                            FirebaseUser user1 = firebaseAuth.getCurrentUser();
                            alert.setP_uid(user1.getUid());
                            alert.setP_name(information.getName());

                            Query query = FirebaseDatabase.getInstance().getReference().child("Alert").orderByChild("c_uid");
                            query.equalTo(alert.getC_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot alertSnapshot : dataSnapshot.getChildren()){
                                        String status = alertSnapshot.child("p_status").getValue().toString();
                                        if(!status.equals("D")){
                                            alertSnapshot.getRef().child("p_status").setValue("O");
                                            alertSnapshot.getRef().child("p_uid").setValue(alert.getP_uid());
                                            alertSnapshot.getRef().child("p_name").setValue(alert.getP_name());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
                );
            }
        });

        holder.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Query query = FirebaseDatabase.getInstance().getReference().child("Alert").orderByChild("c_uid");
                query.equalTo(alert.getC_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot alertSnapshot : dataSnapshot.getChildren()){
                            String status = alertSnapshot.child("p_status").getValue().toString();
                            if(!status.equals("D")){
                                alertSnapshot.getRef().child("p_status").setValue("D");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.buttonViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog MyDialog = new Dialog(mCtx);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.activity_custom_dialog);

                TextView textViewDialogMessage = MyDialog.findViewById(R.id.textViewDialogMessage);
                ImageView imageViewDialogCapture = MyDialog.findViewById(R.id.imageViewDialogCapture);
                Button buttonDialogClose = MyDialog.findViewById(R.id.buttonDialogClose);

                Glide.with(mCtx)
                        .load(alert.getC_capture())
                        .into(imageViewDialogCapture);

                textViewDialogMessage.setText(alert.getC_message());

                buttonDialogClose.setEnabled(true);

                buttonDialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.cancel();
                    }
                });

                MyDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemDialog;
        TextView textViewInfoList;
        ImageView imageViewProfileList;
        Button buttonViewMap, buttonRespond, buttonDone, buttonViewImage;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewInfoList = itemView.findViewById(R.id.textViewInfoList);
            imageViewProfileList = itemView.findViewById(R.id.imageViewProfileList);
            buttonViewMap = itemView.findViewById(R.id.buttonViewMap);
            buttonRespond = itemView.findViewById(R.id.buttonRespond);
            buttonDone = itemView.findViewById(R.id.buttonDone);
            buttonViewImage = itemView.findViewById(R.id.buttonViewImage);
        }
    }
}