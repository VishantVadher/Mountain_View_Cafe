package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class loginFirebase extends AppCompatActivity {



    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button LoginButton, RegisterButton;

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthListner;
    private DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("user");
        mdatabase.keepSynced(true);



        editTextEmail = (EditText) findViewById(R.id.usernameEditText);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);
        LoginButton = (Button) findViewById(R.id.loginButton);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignin();
            }
        });



    }

    private void startSignin() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(loginFirebase.this, "Fields are empty ", Toast.LENGTH_SHORT).show();

        } else {
            Log.d("LOGINSIGNIN", email);
            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                Log.d("EMAIL",  email);
                Log.d("password",  password);

                if (task.isSuccessful()) {
//                    Toast.makeText(loginFirebase.this, "Signed in", Toast.LENGTH_SHORT).show();

                    if( email.toString().equals("admin@mountainviewcafe.com") ) {

                        startActivity(new Intent(getApplicationContext(), adminAddProducts.class ));
                    } else {
                        Toast.makeText(loginFirebase.this, "Logged in" + mauth.getCurrentUser(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), dash.class ));

                    }

                } else {
                    Toast.makeText(loginFirebase.this, "User account dosen't exist", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onStart() {
        super.onStart();

//        if (mauth.getCurrentUser() != null) {
//            Toast.makeText(this, "User is already logged in !", Toast.LENGTH_SHORT).show();
//        }
    }


}