package com.example.pnpemergencyalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<AlertsList> productList;

    //getting the context and product list with constructor
    public AlertsAdapter(Context mCtx, List<AlertsList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_list_alerts, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        AlertsList alertsList = productList.get(position);

        //binding the data with the viewholder views
//        holder.textViewInfo.setText(alertsList.get());
        Glide.with(holder.imageViewProfile.getContext())
            .load(alertsList.getImageUrl())
            .into(holder.imageViewProfile);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInfo;
        ImageView imageViewProfile;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewInfo = itemView.findViewById(R.id.textViewInfo);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
        }
    }
}