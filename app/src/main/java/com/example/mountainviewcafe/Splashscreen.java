package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mauth = FirebaseAuth.getInstance();


        Thread timerThread = new Thread() {
            public void run() {
                mauthListner = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            Log.e("CurrentLogin", firebaseAuth.getCurrentUser().getEmail().toString() );
                            if (firebaseAuth.getCurrentUser().getEmail().toString().equals("admin@mountainviewcafe.com")) {
                                startActivity(new Intent(Splashscreen.this, adminAddProducts.class));

                            } else {
                                startActivity(new Intent(Splashscreen.this, dash.class));
                            }

                        } else {
                            Intent intent = new Intent(getApplicationContext(), loginFirebase.class);
                            startActivity(intent);
                        }

                    }
                };
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthListner);
    }


}
