package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class adminAddProducts extends AppCompatActivity {

    public EditText titleEditText, descEditText, priceEditText, discountEditText;

    Button imageClick, addProduct;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference sref = FirebaseStorage.getInstance().getReference();
    Boolean isImageUpload = false;
    Bitmap imageBitmap;
    ImageView cameraCapture ;
    ImageView imageview;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance() ;
    private FirebaseAuth mAuth;

    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_products);

        addProduct = (Button) findViewById(R.id.addProduct);
        cameraCapture =  (ImageView) findViewById(R.id.cameraClick);

        cameraCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isImageUpload) {
                    byte[] data = convertToByte(imageBitmap);

                    if (isValidCheck()) {

                        titleEditText = (EditText) findViewById(R.id.titleEditText);
                        descEditText = (EditText) findViewById(R.id.descEditText);
                        priceEditText = (EditText) findViewById(R.id.priceEditText);
                        discountEditText = (EditText) findViewById(R.id.discountEditText);

                        final String title = titleEditText.getText().toString().trim();
                        final String desc = descEditText.getText().toString().trim();
                        final String price = priceEditText.getText().toString().trim();
                        final String discount = discountEditText.getText().toString().trim() == null ? "0" : discountEditText.getText().toString().trim() ;
                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Log.d("USERID Firebase auth", userid);

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                        StorageReference imageStorage = storage.getReference();
                        StorageReference imageRef = imageStorage.child("images/" + "cafe_" + timeStamp);

                        Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imageRef.getDownloadUrl();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                Uri downloadUri = task.getResult();
                                String imageURL = downloadUri.toString();
                                Log.e("downloadURI",imageURL);

                                addProduct product = new addProduct(title, desc, imageURL, price, discount);


                                mFirestore.collection("products").document().set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(adminAddProducts.this, "Successfully data to DB", Toast.LENGTH_SHORT).show();
                                            titleEditText.setText("");
                                            descEditText.setText("");
                                            priceEditText.setText("");
                                            discountEditText.setText("");
                                            imageview.setImageDrawable(null);
                                            isImageUpload = false;
                                        } else {
                                            Toast.makeText(adminAddProducts.this, "Error in saving to DB", Toast.LENGTH_SHORT).show();
                                            Log.e("Error", "Error: ",  task.getException());
                                        }
                                    }
                                });

                            } else {
                            }
                        });



                    } else {
                        Toast.makeText(adminAddProducts.this, "Please fill the valid details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(adminAddProducts.this, "Please upload image first !", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public boolean isValidCheck () {
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descEditText = (EditText) findViewById(R.id.descEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        discountEditText = (EditText) findViewById(R.id.discountEditText);

        final String title = titleEditText.getText().toString().trim();
        final String desc = descEditText.getText().toString().trim();
        final String price = priceEditText.getText().toString().trim();
        final String discount = discountEditText.getText().toString().trim();

        boolean isValid = true;

//        if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            editTextEmail.setError("Enter Valid Email Address");
//            isValid = false;
//        } else {
//            editTextEmail.setError(null);
//        }

        if (price.trim().isEmpty()) {
            priceEditText.setError("Enter valid price");
            isValid = false;
        } else {
            priceEditText.setError(null);
        }

        if (title.trim().isEmpty() || title.length() < 2) {
            titleEditText.setError("Enter valid title");
            isValid = false;
        } else {
            titleEditText.setError(null);
        }

        if (desc.trim().isEmpty() || desc.length() < 2) {
            descEditText.setError("Enter valid Description");
            isValid = false;
        } else {
            descEditText.setError(null);
        }
        return isValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginFirebase.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(this, "Successfully Logged out", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageview = (ImageView) findViewById(R.id.viewClickedPic);
            imageview.setImageBitmap(imageBitmap);
            isImageUpload = true;
        } else {
            isImageUpload = false;
        }
    }

    private byte[] convertToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        return data;
    }

//    private void saveImageToFirebase (byte[] data) {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
//        StorageReference imageStorage = storage.getReference();
//        StorageReference imageRef = imageStorage.child("images/" + "cafe_" + timeStamp);
//
//        Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
//            if (!task.isSuccessful()) {
//                throw task.getException();
//            }
//            return imageRef.getDownloadUrl();
//        }).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//
//                Uri downloadUri = task.getResult();
////                imageURL = downloadUri.toString();
//
//            } else {
//                // Handle failures
//                // ...
//            }
//        });
//    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }


}