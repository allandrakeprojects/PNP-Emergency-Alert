package com.example.pnpemergencyalert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editTextProfileFullName, editTextProfileAddress, editTextProfileEmail, editTextProfilePassword;
    public ImageView imageViewProfile, imageViewSideMenuProfile;
    public Button buttonUpdate;
    public Spinner spinnerProfileGender;
    public ProgressDialog progressDialog;
    public TextView textViewSideMenuName, textViewSideMenuType;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(2131296378);
        View headerView = navigationView.getHeaderView(0);

//        Spinner spinner = findViewById(R.id.spinnerProfileGender);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.numbers, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        spinnerProfileGender = (Spinner)findViewById(R.id.spinnerProfileGender);
        editTextProfileFullName = (EditText)findViewById(R.id.editTextProfileFullName);
        editTextProfileAddress = (EditText)findViewById(R.id.editTextProfileAddress);
        editTextProfileEmail = (EditText)findViewById(R.id.editTextProfileEmail);
        editTextProfilePassword = (EditText)findViewById(R.id.editTextProfilePassword);
        imageViewProfile = (ImageView)findViewById(R.id.imageViewProfile);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        textViewSideMenuName = (TextView)headerView.findViewById(R.id.textViewSideMenuName);
        textViewSideMenuType = (TextView)headerView.findViewById(R.id.textViewSideMenuType);
        imageViewSideMenuProfile = (ImageView)headerView.findViewById(R.id.imageViewSideMenuProfile);


        progressDialog = new ProgressDialog(this);
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
                textViewSideMenuName.setText(information.getName());
                if(information.getType().equals("C")){
                    textViewSideMenuType.setText("Citizen");
                } else if(information.getType().equals("A")){
                    textViewSideMenuType.setText("Administrator");
                } else if(information.getType().equals("S")){
                    textViewSideMenuType.setText("SUPERVISOR");
                }
                Glide.with(getApplicationContext())
                    .load(information.getImageUrl())
                    .into(imageViewSideMenuProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
//        if (id == R.id.nav_alert) {
//
//        } else if (id == R.id.nav_profile_settinngs) {
//
//        } else if (id == R.id.nav_how_to_use) {
//
//        } else if (id == R.id.nav_signout) {
//
//        }
        if (id == R.id.nav_signout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_alert:
                fragment = new FragmentMenu01();
                break;
            case R.id.nav_profile_settinngs:
                fragment = new FragmentMenu02();
                break;
            case R.id.nav_how_to_use:
                fragment = new FragmentMenu03();
                break;
//            case R.id.nav_signout:
//                fragment = new Menu3();
//                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
