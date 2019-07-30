package com.example.pnpemergencyalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import pl.droidsonroids.gif.GifImageView;

public class FragmentMenu01 extends Fragment {

    private ImageView imageViewAlert, imageViewProfile;
    private GifImageView gifAlert;
    private CardView cardViewInfo, cardViewAlert;
    private TextView textViewName, textViewStatus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_01, container, false);
        init(view);
        return view;
    }


    private void init(View view){
        imageViewAlert = (ImageView)view.findViewById(R.id.imageViewAlert);
        gifAlert = (GifImageView)view.findViewById(R.id.gifAlert);
        cardViewInfo = (CardView) view.findViewById(R.id.cardViewInfo);
        cardViewAlert = (CardView) view.findViewById(R.id.cardViewAlert);

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
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Send Alert to PNP?").setPositiveButton("No", dialogClickListener)
                        .setNegativeButton("Yes", dialogClickListener).show();
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