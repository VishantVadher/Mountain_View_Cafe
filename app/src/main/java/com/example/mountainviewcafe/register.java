package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, "User is already logged in !", Toast.LENGTH_SHORT).show();
        }

        /*final EditText dob = (EditText) findViewById(R.id.birthday);
        final TextView age = (TextView)findViewById(R.id.age);
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }

            }
        });*/
    }
}