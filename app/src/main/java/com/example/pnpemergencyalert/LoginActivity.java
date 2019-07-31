package com.example.pnpemergencyalert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Widgets
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        progressDialog = new ProgressDialog(this);

        INIT();
    }

    public void INIT(){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.contains("@")){
                    Toast.makeText(LoginActivity.this, "Please provide a valid email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Logging in...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // redirect to home
                        finish();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Email or Password didn't match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    }
                });
            }
        });
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
}
