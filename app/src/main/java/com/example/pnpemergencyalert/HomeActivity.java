package com.example.pnpemergencyalert;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editTextProfileFullName, editTextProfileAddress, editTextProfileEmail, editTextProfilePassword;
    public ImageView imageViewProfile, imageViewSideMenuProfile;
    public Button buttonUpdate;
    public Spinner spinnerProfileGender;
    public ProgressDialog progressDialog;
    public TextView textViewSideMenuName, textViewSideMenuType;

    public Menu menu;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    DBAdapter helper;

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

//        createNetErrorDialog();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

        helper = new DBAdapter(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting info...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Information information = dataSnapshot.getValue(Information.class);
                textViewSideMenuName.setText(information.getName());
                if(information.getType().equals("C")){
                    textViewSideMenuType.setText("Citizen");
                    Fragment fragment = new FragmentMenu01();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    navigationView.getMenu().getItem(3).setChecked(true);

                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_send_alert).setVisible(true);
                    nav_Menu.findItem(R.id.nav_profile_settings).setVisible(true);
                    nav_Menu.findItem(R.id.nav_alert).setVisible(false);
                    nav_Menu.findItem(R.id.nav_history).setVisible(false);
                    nav_Menu.findItem(R.id.nav_police_officer).setVisible(false);

                } else if(information.getType().equals("A")){
                    textViewSideMenuType.setText("ADMINISTRATOR");
                    Fragment fragment = new FragmentMenu04();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    navigationView.getMenu().getItem(0).setChecked(true);

                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_send_alert).setVisible(false);
                    nav_Menu.findItem(R.id.nav_profile_settings).setVisible(false);
                    nav_Menu.findItem(R.id.nav_alert).setVisible(true);
                    nav_Menu.findItem(R.id.nav_history).setVisible(true);
                    nav_Menu.findItem(R.id.nav_police_officer).setVisible(true);
                } else if(information.getType().equals("P")){
                    textViewSideMenuType.setText("Police Officer");
                    Fragment fragment = new FragmentMenu04();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    navigationView.getMenu().getItem(0).setChecked(true);

                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_send_alert).setVisible(false);
                    nav_Menu.findItem(R.id.nav_profile_settings).setVisible(false);
                    nav_Menu.findItem(R.id.nav_alert).setVisible(true);
                    nav_Menu.findItem(R.id.nav_history).setVisible(true);
                    nav_Menu.findItem(R.id.nav_profile_settings).setVisible(true);
                    nav_Menu.findItem(R.id.nav_police_officer).setVisible(false);
                }
                Glide.with(getApplicationContext())
                    .load(information.getImageUrl())
                    .into(imageViewSideMenuProfile);

                if(!information.getType().equals("C")){
                    Query ref = FirebaseDatabase.getInstance().getReference().child("Alert");
                    ref.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = 0;
                            String last_id = "";

                            for(com.google.firebase.database.DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                String p_status = singleSnapshot.child("p_status").getValue(String.class);
                                if(!p_status.equals("D")){
                                    last_id = singleSnapshot.getKey();
                                    count++;
                                }
                            }

                            if(count != 0){
                                final Cursor res = helper.getAllData();
                                if(res!=null && res.getCount()>0){
                                    if (res.moveToFirst()) {
                                        if(!res.getString(1).equals(last_id)){
                                            NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
                                            b.setAutoCancel(true)
                                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setSmallIcon(R.drawable.ic_alert)
                                                    .setTicker("PNP Emergency Alert")
                                                    .setContentTitle("PNP Emergency Alert")
                                                    .setContentText("Someone need help.");
                                            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            nm.notify(1, b.build());

                                            helper.updateUser(last_id);
                                        }
                                    }
                                } else{
                                    boolean isInserted = helper.insertData(last_id);
                                    if(isInserted){
                                        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
                                        b.setAutoCancel(true)
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setWhen(System.currentTimeMillis())
                                                .setSmallIcon(R.drawable.ic_alert)
                                                .setTicker("PNP Emergency Alert")
                                                .setContentTitle("PNP Emergency Alert")
                                                .setContentText("Someone need help.");
                                        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        nm.notify(1, b.build());
                                    } else{
                                        Toast.makeText(HomeActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Query query = FirebaseDatabase.getInstance().getReference().child("Alert").orderByChild("c_uid");
        query.equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot alertSnapshot : dataSnapshot.getChildren()){
                    String c_uid = alertSnapshot.child("c_uid").getValue().toString();
                    String status = alertSnapshot.child("p_status").getValue().toString();
                    if(!status.equals("D") && c_uid.equals(firebaseAuth.getUid())){
                        Boolean read = (Boolean) alertSnapshot.child("c_read").getValue();
                        if(status.equals("O")){
                            if(!read){
                                NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
                                b.setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.ic_alert)
                                        .setTicker("PNP Emergency Alert")
                                        .setContentTitle("PNP Emergency Alert")
                                        .setContentText("Our police officer is on the way now.");
                                NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                nm.notify(1, b.build());

                                alertSnapshot.getRef().child("c_read").setValue(true);
                            }
                        }

                        break;
                    }
                }
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
        this.menu = menu;
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
            case R.id.nav_send_alert:
                fragment = new FragmentMenu01();
                break;
            case R.id.nav_profile_settings:
                fragment = new FragmentMenu02();
                break;
            case R.id.nav_how_to_use:
                fragment = new FragmentMenu03();
                break;
            case R.id.nav_alert:
                fragment = new FragmentMenu04();
                break;
            case R.id.nav_history:
                fragment = new FragmentMenu05();
                break;
            case R.id.nav_police_officer:
                fragment = new FragmentMenu06();
                break;
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



    protected void createNetErrorDialog() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need internet connection for this app. Please turn on mobile network or Wi-Fi in Settings.")
                    .setTitle("Unable to connect")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    HomeActivity.this.finish();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
