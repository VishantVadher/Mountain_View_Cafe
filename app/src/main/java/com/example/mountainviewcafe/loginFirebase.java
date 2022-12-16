package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView createAccount;
    private Button LoginButton, RegisterButton;

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthListner;
    private DatabaseReference mdatabase;


    @SuppressLint("MissingInflatedId")
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

        createAccount = (TextView) findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), register.class ));
            }
        });

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

        if (validationCheck()) {
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
        } else {
            Toast.makeText(this, "Please check for errors.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStart() {
        super.onStart();

//        if (mauth.getCurrentUser() != null) {
//            Toast.makeText(this, "User is already logged in !", Toast.LENGTH_SHORT).show();
//        }
    }

    public boolean validationCheck() {

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        boolean valid = true;

        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter Valid Email Address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (password.trim().isEmpty() || password.length() < 6) {
            editTextPassword.setError("Enter Valid Password more than 6 Alphabets");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }



}