package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
    String imageURL;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


    private Uri mImageUri = null;
    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_products);

        imageClick = (Button) findViewById(R.id.clickPhoto);
        addProduct = (Button) findViewById(R.id.addProduct);

        imageClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEditText = (EditText) findViewById(R.id.titleEditText);
                descEditText = (EditText) findViewById(R.id.descEditText);
                priceEditText = (EditText) findViewById(R.id.priceEditText);
                discountEditText = (EditText) findViewById(R.id.discountEditText);

                final String title = titleEditText.getText().toString().trim();
                final String desc = descEditText.getText().toString().trim();
                final String price = priceEditText.getText().toString().trim();
                final String discount = discountEditText.getText().toString().trim();

                if(isImageUpload) {

                    firebaseUploadBitmap(imageBitmap);

                    if (imageURL.length() > 0) {
                        addProduct product = new addProduct(title, desc, imageURL, price, discount);

                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Log.d("USERID Firebase auth", userid);

                        mFirestore.collection("products").document(userid).set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(adminAddProducts.this, "Successfully saved to DB", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(adminAddProducts.this, "Error in saving to DB", Toast.LENGTH_SHORT).show();
                                    Log.e("Error", "Error: ",  task.getException());
                                }
                            }
                        });

                    } else {
                        Toast.makeText(adminAddProducts.this, "Problem with image file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(adminAddProducts.this, "Problem in image", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            imageBitmap = (Bitmap) data.getExtras().get("data");

            Log.e("IMAGE", String.valueOf(imageBitmap) );

            ImageView imageview = (ImageView) findViewById(R.id.viewClickedPic);
            imageview.setImageBitmap(imageBitmap);
            isImageUpload = true;
//            firebaseUploadBitmap(imageBitmap);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void firebaseUploadBitmap(Bitmap bitmap) {
        String urlData;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        StorageReference imageStorage = storage.getReference();
        StorageReference imageRef = imageStorage.child("images/" + "cafe_" + timeStamp);

        Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                imageURL = downloadUri.toString();
            } else {
                // Handle failures
                // ...
            }
        });

        Log.e("URLTASKFINAL",urlTask.toString() );
    }

}