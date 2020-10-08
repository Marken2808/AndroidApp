package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText us,pw;
    private Button sIn, sUp;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fAuth = FirebaseAuth.getInstance();
        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if( user != null) {
                    Intent intent = new Intent(SignInActivity.this , InsideActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        us = findViewById(R.id.username);
        pw = findViewById(R.id.password);
        sIn = findViewById(R.id.signIn);
        sUp = findViewById(R.id.signUp);

        sUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = us.getText().toString();
                final String pass = pw.getText().toString();
                fAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            showToast("Sign Up error");
                        } else {
                            String userID = fAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            currentUserDB.setValue(true);
                        }
                    }
                });
            }
        });

        sIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = us.getText().toString();
                final String pass = pw.getText().toString();
                fAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showToast("Sign In error");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fAuth.removeAuthStateListener(fAuthListener);
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}