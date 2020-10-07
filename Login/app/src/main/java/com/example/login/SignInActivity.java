package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    EditText us,pw;
    Button sIn, sUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        us = findViewById(R.id.username);
        pw = findViewById(R.id.password);
        sIn = findViewById(R.id.signIn);
        sUp = findViewById(R.id.signUp);
    }
}