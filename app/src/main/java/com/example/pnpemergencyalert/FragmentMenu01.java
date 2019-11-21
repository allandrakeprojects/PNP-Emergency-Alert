package com.example.pnpemergencyalert;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.client.Firebase;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pl.droidsonroids.gif.GifImageView;

public class FragmentMenu01 extends Fragment {

    private ImageView imageViewAlert, imageViewProfile, imageViewAlertCapture01, imageViewAlertCapture02, imageViewAlertCapture03;
    private GifImageView gifAlert;
    private CardView cardViewInfo, cardViewAlert;
    private TextView textViewName, textViewStatus;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public static LatLng latLng;
    private static final String TAG = "test";
    Cursor cursorMsg, cursorDtl;
    String name;
    String imageUrl;
    public static String message;
    public static final int RequestPermissionCode  = 1 ;
    String activeUser;
    String color;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/PNP Emergency Alert";
    private Uri contentURI;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private EditText input;

    String selectedTypeofIncident;
    String selectedCameraOrGallery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_01, container, false);
        onAttach(getContext());
        //Firebase
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        init(view);
        return view;
    }
    private void init(View view){
        imageViewAlert = (ImageView)view.findViewById(R.id.imageViewAlert);
        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        imageViewAlertCapture01 = (ImageView)view.findViewById(R.id.imageViewAlertCapture01);
        imageViewAlertCapture02 = (ImageView)view.findViewById(R.id.imageViewAlertCapture02);
        imageViewAlertCapture03 = (ImageView)view.findViewById(R.id.imageViewAlertCapture03);
        gifAlert = (GifImageView)view.findViewById(R.id.gifAlert);
        cardViewInfo = (CardView) view.findViewById(R.id.cardViewInfo);
        cardViewAlert = (CardView) view.findViewById(R.id.cardViewAlert);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

        imageViewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                final String[] listItems = { "Driver’s Errors ", "Mechanical Defect", "Over Speeding", "Drunk Driver", "Damaged Roads" };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Type of Incident");

                                int checkedItem = 0; //this will checked the item when user open the dialog
                                builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedTypeofIncident = listItems[which];
                                    }
                                });

                                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        choosePhoto();
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("PNP Emergency Alert").setNegativeButton("Cancel", dialogClickListener)
                        .setPositiveButton("Report an incident", dialogClickListener).show();
            }
        });

        final Query query = FirebaseDatabase.getInstance().getReference().child("Alert").orderByChild("c_uid");
        query.equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean detectD = false;
                for(DataSnapshot alertSnapshot : dataSnapshot.getChildren()){
                    String status = alertSnapshot.child("p_status").getValue().toString();
                    if(!status.equals("D") && !status.equals("X")){
                        detectD = true;
                        cardViewAlert.setVisibility(View.INVISIBLE);
                        cardViewInfo.setVisibility(View.VISIBLE);

                        Boolean read = (Boolean) alertSnapshot.child("c_read").getValue();
                        String p_uid = alertSnapshot.child("p_uid").getValue().toString();

                        if(status.equals("W")){
                            textViewStatus.setText("Status: Waiting");
                        } else if(status.equals("O")){
                            textViewStatus.setText("Status: On the way");
                        }

                        // Get police officer if exists yet
                        try{
                            if(!p_uid.equals("-")){
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + p_uid);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Information information = dataSnapshot.getValue(Information.class);
                                        textViewName.setText("Police Officer: " + information.getName());
                                        final Context context = getContext();
                                        if (isValidContextForGlide(context)) {
                                            Glide.with(context)
                                                .load(information.getImageUrl())
                                                .into(imageViewProfile);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else{
                                textViewName.setText("Police Officer: -");
                                imageViewProfile.setImageResource(R.drawable.baseline_account_circle_black_48);
                            }
                        } catch (Exception err){
                            textViewName.setText("Police Officer: -");
                            textViewStatus.setText("Status: -");
                            imageViewProfile.setImageResource(R.drawable.baseline_account_circle_black_48);
//                            imageViewProfile.setImageDrawable(FragmentMenu01.this.getContext().getDrawable(R.drawable.baseline_account_circle_black_48));
                            cardViewAlert.setVisibility(View.VISIBLE);
                            cardViewInfo.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                if(!detectD){
                    textViewName.setText("Police Officer: -");
                    textViewStatus.setText("Status: -");
                    imageViewProfile.setImageResource(R.drawable.baseline_account_circle_black_48);
//                            imageViewProfile.setImageDrawable(FragmentMenu01.this.getContext().getDrawable(R.drawable.baseline_account_circle_black_48));
                    cardViewAlert.setVisibility(View.VISIBLE);
                    cardViewInfo.setVisibility(View.INVISIBLE);
                }


//                } catch (Exception err){
////                    textViewName.setText("Police Officer: -");
////                    textViewStatus.setText("Status: -");
////                    imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.baseline_account_circle_black_48));
//                    cardViewAlert.setVisibility(View.VISIBLE);
//                    cardViewInfo.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }

    private void getDeviceLocation(final String downloadURI){
        Log.d(TAG, "getDeviceLocation: getting the device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            String asd1 = String.valueOf(currentLocation.getLatitude());
                            String asd2 = String.valueOf(currentLocation.getLongitude());
                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/" + user.getUid());
                            databaseReference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Date today = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String dateToStr = format.format(today);

                                    Information information = dataSnapshot.getValue(Information.class);
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String parent_id = databaseReference.child("transaction").push().getKey();
                                    Alert alert = new Alert(parent_id, user.getUid(), information.getName(), information.getImageUrl(), latLng.latitude + "", latLng.longitude + "", dateToStr, "-", "-", "W", selectedTypeofIncident, downloadURI, false);
                                    String id = databaseReference.child("Alert").push().getKey();
                                    databaseReference.child("Alert").child(parent_id).setValue(alert);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                ;
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception: "+e.getMessage());
        }

    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            } else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void showPictureDialog(){
        takePhotoFromCamera();
    }

    ArrayList<String> filePaths = new ArrayList<>();

    public void choosePhoto() {
        filePaths.clear();
        FilePickerBuilder.getInstance().setMaxCount(3)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.Theme_AppCompat_DayNight)
                .pickPhoto(this);
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public int PIC_CODE = 0;
    public String downloadURI = "";

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == getActivity().RESULT_CANCELED) {
//            if(PIC_CODE != 0){
//                cardViewAlert.setVisibility(View.INVISIBLE);
//                cardViewInfo.setVisibility(View.VISIBLE);
//            }
//            if(PIC_CODE == 1){
//                uploadFile01();
//            } else if(PIC_CODE == 2){
//                uploadFile02();
//            } else if(PIC_CODE == 3){
//                uploadFile03();
//            }
//            return;
//        }
//
//        Toast.makeText(getActivity(), "test teasdsast 123", Toast.LENGTH_SHORT).show();
//        Log.d("test", "teasdsast");
//
//        switch (requestCode){
//            case FilePickerConst.REQUEST_CODE:
//                Toast.makeText(getActivity(), "test detect 123", Toast.LENGTH_SHORT).show();
//                Log.d("test", "test");
//        }
//
//        if (requestCode == FilePickerConst.REQUEST_CODE) {
//            Toast.makeText(getActivity(), "test detect 123", Toast.LENGTH_SHORT).show();
//            Log.d("test", "dasdas123");
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        else if (requestCode == CAMERA) {
//            if(PIC_CODE < 3){
//                PIC_CODE++;
//
//                if(PIC_CODE == 1){
//                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                    imageViewAlertCapture01.setImageBitmap(thumbnail);
//                    saveImage(thumbnail);
//                    imageViewAlertCapture01.setTag("changesImage");
//
//                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, CAMERA);
//                } else if(PIC_CODE == 2) {
//                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                    imageViewAlertCapture02.setImageBitmap(thumbnail);
//                    saveImage(thumbnail);
//                    imageViewAlertCapture02.setTag("changesImage");
//
//                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, CAMERA);
//                } else if(PIC_CODE == 3) {
//                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                    imageViewAlertCapture03.setImageBitmap(thumbnail);
//                    saveImage(thumbnail);
//                    imageViewAlertCapture03.setTag("changesImage");
//                }
//            }
//
////            uploadFile();
////            Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//
//            if(PIC_CODE == 3){
//                cardViewAlert.setVisibility(View.INVISIBLE);
//                cardViewInfo.setVisibility(View.VISIBLE);
//                uploadFile03();
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    try {
                        for(String path : filePaths) {
                            PIC_CODE++;

                            if(PIC_CODE == 1){
                                File file = new  File(path);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                imageViewAlertCapture01.setImageBitmap(bitmap);
                            } else if(PIC_CODE == 2){
                                File file = new  File(path);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                imageViewAlertCapture02.setImageBitmap(bitmap);
                            } else if(PIC_CODE == 3){
                                File file = new  File(path);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                imageViewAlertCapture03.setImageBitmap(bitmap);
                            }
                        }

                        if(PIC_CODE == 1){
                            uploadFile01();
                        } else if(PIC_CODE == 2){
                            uploadFile02();
                        } else if(PIC_CODE == 3){
                            uploadFile03();
                        }

                        if(PIC_CODE != 0){
                            cardViewAlert.setVisibility(View.INVISIBLE);
                            cardViewInfo.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), "No Selected Photo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception err){
                        Log.d("test", "Error " + err.getMessage());
                    }

                } else {
                    Toast.makeText(getContext(), "No Selected Photo.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }










    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void uploadFile01() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture01.setDrawingCacheEnabled(true);
        imageViewAlertCapture01.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture01.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI = task.getResult().toString();
                    getLocationPermission();
                    getDeviceLocation(downloadURI);
                    PIC_CODE = 0;
                    downloadURI = "";
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void uploadFile02() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture01.setDrawingCacheEnabled(true);
        imageViewAlertCapture01.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture01.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI += task.getResult().toString() + ',';
                    uploadFile022();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void uploadFile022() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture02.setDrawingCacheEnabled(true);
        imageViewAlertCapture02.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture02.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI += task.getResult().toString();
                    getLocationPermission();
                    getDeviceLocation(downloadURI);
                    PIC_CODE = 0;
                    downloadURI = "";
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void uploadFile03() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture01.setDrawingCacheEnabled(true);
        imageViewAlertCapture01.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture01.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI += task.getResult().toString() + ',';
                    uploadFile0222();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void uploadFile0222() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture02.setDrawingCacheEnabled(true);
        imageViewAlertCapture02.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture02.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI += task.getResult().toString() + ',';
                    uploadFile0333();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void uploadFile0333() {
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageViewAlertCapture03.setDrawingCacheEnabled(true);
        imageViewAlertCapture03.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewAlertCapture03.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference1.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURI += task.getResult().toString() + ',';
                    getLocationPermission();
                    getDeviceLocation(downloadURI);
                    PIC_CODE = 0;
                    downloadURI = "";
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}