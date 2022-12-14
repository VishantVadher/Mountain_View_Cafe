package com.example.mountainviewcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

//        mActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
//        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//            }
//        });


        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                                    detailPrice.setText("$" + document.get("price").toString());

                                    Picasso.get().load(document.get("image").toString()).into(detailImage);

                                    detailAddToCart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cartAdd cartAdd = new cartAdd(idIntent.getStringExtra("id"),userid.toString(),"1",  document.get("title").toString(),
                                                    document.get("discount").toString(), document.get("image").toString(), document.get("description").toString(),
                                                    document.get("price").toString());

                                            mFirestore.collection("Cart").document().set(cartAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(detailView.this, dash.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Error", "onFailure: " + e );
                                                }
                                            });
                                        }
                                    });

                                }
                            } else {
                                Log.d("ERROR", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        } else {
        }

    }
}