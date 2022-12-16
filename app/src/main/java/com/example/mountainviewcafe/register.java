package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class register extends AppCompatActivity {

    public EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextPhone;
    public Button registerButton;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Firebase intitialization !
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        editTextFirstName = (EditText) findViewById(R.id.registerFirstnameEditText);
        editTextLastName = (EditText) findViewById(R.id.registerLastnameEditText);
        editTextEmail = (EditText) findViewById(R.id.registerEmailEditText);
        editTextPassword = (EditText) findViewById(R.id.registerPasswordEdittext);
        editTextPhone = (EditText) findViewById(R.id.registerPhoneEditText);
        registerButton = (Button) findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = editTextFirstName.getText().toString().trim();
                final String lastName = editTextLastName.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                final String phone = editTextPhone.getText().toString().trim();

                try {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                userRegister userregister = new userRegister( firstName,lastName, email, password, phone );

                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Log.d("USERID", userid);


                                mFirestore.collection("user").document(userid).set(userregister).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(register.this, "Successfully Store to DB", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), loginFirebase.class );
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                Log.e("Err in DB Conn", "onClick: ", task.getException());

                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e("Err in DB Conn", "onClick: ", e);
                }

            }
        });

    }

    public void onStart() {
        super.onStart();

//        if (mAuth.getCurrentUser() != null) {
//            Toast.makeText(this, "User is already logged in !", Toast.LENGTH_SHORT).show();
//        }
    }

    public boolean check() {
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        boolean valid = true;

        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter Valid Email Address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (phone.trim().isEmpty() || phone.length() != 10) {
            editTextPhone.setError("Enter Valid Phone Number");
            valid = false;
        } else {
            editTextPhone.setError(null);
        }

        if (password.trim().isEmpty() || password.length() < 6) {
            editTextPassword.setError("Enter Valid Password more than 6 Alphabets");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        if (firstName.trim().isEmpty() || firstName.matches("\\d+(?:\\.\\d+)?")) {
            editTextFirstName.setError("Please Enter valid First Name");
            valid = false;
        } else {
            editTextFirstName.setError(null);
        }

        if (lastName.trim().isEmpty() || lastName.matches("\\d+(?:\\.\\d+)?")) {
            editTextLastName.setError("Please Enter valid Last Name");
            valid = false;
        } else {
            editTextLastName.setError(null);
        }

        return valid;
    }
}