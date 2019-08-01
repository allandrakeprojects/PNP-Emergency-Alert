package com.example.pnpemergencyalert;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

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
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final Alerts alerts = alertsList.get(position);

        //binding the data with the viewholder views
        holder.textViewInfoList.setText(alerts.getName() + "\n" + alerts.getDatetime() + "\nPolice Officer: " + alerts.getPolice_name() + "\nStatus: " + alerts.getStatus());
        holder.buttonViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.google.com/maps/place/"+alerts.getLat()+","+alerts.getLng();
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
            .load(alerts.getImageUrl())
            .into(holder.imageViewProfileList);
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