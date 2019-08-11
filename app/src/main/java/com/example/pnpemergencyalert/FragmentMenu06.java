package com.example.pnpemergencyalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

public class FragmentMenu06 extends Fragment {

    private EditText editTextPoliceFullName, editTextPoliceAddress, editTextPoliceEmail, editTextPolicePassword;
    private ImageView imageViewPolice, imageViewSideMenuPolice;
    private Button buttonPoliceRegister;
    private Spinner spinnerPoliceGender;
    private ProgressDialog progressDialog;
    private RelativeLayout buttonPicturePolice;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/PNP Emergency Alert";
    private String imageURL = "";
    private String type = "";
    private boolean isImageChanges = false;
    public TextView textViewSideMenuName, textViewSideMenuType;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUploads;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_06, container, false);
        init(view);
        return view;
    }


    private void init(View view){
        Spinner spinner = view.findViewById(R.id.spinnerPoliceGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.nav_header_home, navigationView, false);


        spinnerPoliceGender = (Spinner)view.findViewById(R.id.spinnerPoliceGender);
        editTextPoliceFullName = (EditText)view.findViewById(R.id.editTextPoliceFullName);
        editTextPoliceAddress = (EditText)view.findViewById(R.id.editTextPoliceAddress);
        editTextPoliceEmail = (EditText)view.findViewById(R.id.editTextPoliceEmail);
        editTextPolicePassword = (EditText)view.findViewById(R.id.editTextPolicePassword);
        imageViewPolice = (ImageView)view.findViewById(R.id.imageViewPoliceProfile);
        buttonPoliceRegister = (Button)view.findViewById(R.id.buttonPoliceRegister);
        progressDialog = new ProgressDialog(view.getContext());

        textViewSideMenuName = (TextView)headerView.findViewById(R.id.textViewSideMenuName);
        textViewSideMenuType = (TextView)headerView.findViewById(R.id.textViewSideMenuType);
//        imageViewSideMenuPolice = (ImageView)headerView.findViewById(R.id.imageViewSideMenuPolice);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceUploads = FirebaseDatabase.getInstance().getReference("uploads");

        buttonPicturePolice = (RelativeLayout)view.findViewById(R.id.picturePolice);
        buttonPicturePolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        buttonPoliceRegister = (Button)view.findViewById(R.id.buttonPoliceRegister);
        buttonPoliceRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextPoliceFullName.getText().toString().trim();
                String address = editTextPoliceAddress.getText().toString().trim();
                String email = editTextPoliceEmail.getText().toString().trim();
                String pass = editTextPolicePassword.getText().toString().trim();
                String gender;
                try {
                    gender = spinnerPoliceGender.getSelectedItem().toString();
                } catch(Exception n){
                    gender = "";
                }

//                Validation
                if(!isImageChanges){
                    Toast.makeText(getContext(), "Please select a photo of you.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String backgroundImageName = String.valueOf(imageViewPolice.getTag());
                if(backgroundImageName.equals("defaultImage")){
                    Toast.makeText(getContext(), "Please select a photo of you.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(getContext(), "Please enter your full name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    Toast.makeText(getContext(), "Please enter your address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(), "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.contains("@")){
                    Toast.makeText(getContext(), "Please provide a valid email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gender.equals("Select Gender")){
                    Toast.makeText(getContext(), "Please select your gender.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_NEGATIVE:
                                progressDialog.setMessage("Making for police info...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();

                                String email = editTextPoliceEmail.getText().toString().trim();
                                String pass = editTextPolicePassword.getText().toString().trim();

                                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            saveUser();
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                isImageChanges = false;
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Is all information correct?").setPositiveButton("Wait", dialogClickListener)
                        .setNegativeButton("Continue", dialogClickListener).show();
            }
        });
    }

    private void saveUser(){

        uploadFile();
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

    private void changePassword(){
        String pass = editTextPolicePassword.getText().toString().trim();
        if(!pass.equals("dEFaultPasSW0Rd16")){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                }
            });
        }
    }

    private void uploadFile(){
        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + ".jpg");

        imageViewPolice.setDrawingCacheEnabled(true);
        imageViewPolice.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewPolice.getDrawable()).getBitmap();
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
                progressDialog.dismiss();
                isImageChanges = false;
                Toast.makeText(getContext(), "Your information already saved!", Toast.LENGTH_SHORT).show();

                // redirect to home
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
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
                    Uri downloadUri = task.getResult();

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setProgress(0);
//                        }
//                    }, 500);

                    String fullName = editTextPoliceFullName.getText().toString().trim();
                    String address = editTextPoliceAddress.getText().toString().trim();
                    String email = editTextPoliceEmail.getText().toString().trim();
                    String gender;

                    try {
                        gender = spinnerPoliceGender.getSelectedItem().toString();
                    } catch(Exception n){
                        gender = "";
                    }

                    Information information = new Information(fullName, address, email, gender, downloadUri.toString(), "P");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child("Users").child(user.getUid()).setValue(information);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

//        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e){
//                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
//            }
//        })
//        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//    //                    progressBar.setProgress((int) progress);
//            }
//        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
//                    String path = saveImage(bitmap);
                    isImageChanges = true;
//                    Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageViewPolice.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
//                    Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageViewPolice.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            isImageChanges = true;
//            Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    // a static variable to get a reference of our application context
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
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
}
