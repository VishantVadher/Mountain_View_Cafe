package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class detailView extends AppCompatActivity {

    public String id;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mauth;

    Button detailAddToCart;
    ImageView detailImage;
    TextView detailTitle, detailPrice, detailDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        mFirestore = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        Intent idIntent = getIntent();
        id =  idIntent.getStringExtra("id");
        Log.e("idCAMETODETAIL", idIntent.getStringExtra("id"));

        if(id.length() > 0 && id != null) {

            mFirestore.collection("products")
                    .whereEqualTo( "__name__" , idIntent.getStringExtra("id"))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("ID", document.getId() + " => " + document.getData());

                                    detailTitle = (TextView) findViewById(R.id.detailTitle);
                                    detailDesc = (TextView) findViewById(R.id.detailDesc);
                                    detailPrice = (TextView) findViewById(R.id.detailPrice);
                                    detailAddToCart = (Button) findViewById(R.id.detailAddToCart);
                                    detailImage = (ImageView) findViewById(R.id.detailImage);

                                    detailTitle.setText(document.get("title").toString());
                                    detailDesc.setText(document.get("description").toString());
                                    detailPrice.setText(document.get("price").toString());

                                    Picasso.get().load(document.get("image").toString()).into(detailImage);

                                }
                            } else {
                                Log.d("ERROR", "Error getting documents: ", task.getException());
                            }
                        }
                    });



//            DocumentReference docRef = mFirestore.collection("products").document(id);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.e("DETAILTASK", task.toString() );
//
////                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        } else {
////                            Log.d(TAG, "No such document");
//                        }
//                    } else {
////                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });




        } else {
        }

    }
}