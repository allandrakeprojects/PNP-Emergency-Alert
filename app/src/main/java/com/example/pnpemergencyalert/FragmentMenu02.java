package com.example.pnpemergencyalert;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentMenu02 extends Fragment {

    private EditText editTextProfileFullName, editTextProfileAddress, editTextProfileEmail, editTextProfilePassword;
    private ImageView imageViewProfile;
    private Button buttonUpdate;
    private Spinner spinnerProfileGender;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_02, container, false);
        init(view);
        return view;
    }


    private void init(View view){
        Spinner spinner = view.findViewById(R.id.spinnerProfileGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinnerProfileGender = (Spinner)view.findViewById(R.id.spinnerProfileGender);
        editTextProfileFullName = (EditText)view.findViewById(R.id.editTextProfileFullName);
        editTextProfileAddress = (EditText)view.findViewById(R.id.editTextProfileAddress);
        editTextProfileEmail = (EditText)view.findViewById(R.id.editTextProfileEmail);
        editTextProfilePassword = (EditText)view.findViewById(R.id.editTextProfilePassword);
        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        buttonUpdate = (Button)view.findViewById(R.id.buttonUpdate);
        progressDialog = new ProgressDialog(view.getContext());

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
                editTextProfileFullName.setText(information.getName());
                editTextProfileAddress.setText(information.getAddress());
                editTextProfileEmail.setText(information.getEmail());
                editTextProfilePassword.setText("123456789");
                if(information.getGender() == "Male"){
                    spinnerProfileGender.setSelection(1);
                } else{
                    spinnerProfileGender.setSelection(2);
                }
                Glide.with(getContext())
                        .load(information.getImageUrl())
                        .into(imageViewProfile);

//                Log.d("testtesttest", information.getName());
//                Log.d("testtesttest", information.getGender());
//                Log.d("testtesttest", information.getAddress());
//                Log.d("testtesttest", information.getEmail());
//                Log.d("testtesttest", information.getType());
//                Log.d("testtesttest", information.getImageUrl());
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
