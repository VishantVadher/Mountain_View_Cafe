package com.example.mountainviewcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class dash extends AppCompatActivity {


    MyAdapter adapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mauth;
    public ArrayList<addProduct> myProductList = new ArrayList<>();
//    private ImageView imageView;
//    String title="", image = "", description="", price = "", discount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mFirestore = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.imagePhoto);
//        Picasso.get().setLoggingEnabled(true);
//        Picasso.get()
//                .load(R.drawable.camera_icon)
//                .into(imageView);
//        Glide.with(this)
//            .load("https://images.unsplash.com/photo-1500100586562-f75ff6540087?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3589&q=80")
//                .into(imageView);

        Glide.with(this).load("https://images.unsplash.com/photo-1500100586562-f75ff6540087?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3589&q=80")
                .dontAnimate().into(imageView);



        final String appuid = FirebaseAuth.getInstance().getUid();
        mFirestore.collection("products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(dash.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("SIZEE", "onEvent: " + queryDocumentSnapshots.size() );

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {



                    addProduct product = doc.toObject(addProduct.class);
                    myProductList.add(product);
//                    myProductList.add(new addProduct(new addProduct("","","","",""));
//                    title = doc.get("title").toString();
//                    description = doc.get("description").toString();
//                    image = doc.get("image").toString();
//                    price = doc.get("price").toString();
//                    discount = doc.get("discount").toString();
//                    myProductList.add(new addProduct(title, description, image, price, discount));
//                    myProductList.add(new addProduct(doc.get("title").toString(), doc.get("description").toString(),doc.get("image").toString(),doc.get("price").toString(),doc.get("discount").toString()));
//                    Log.e("DOC", doc.get("id").toString() );
                    Log.e("myProductList", doc.toString() );
                    Log.e("document id ", doc.getId()  );

                }

                adapter = new MyAdapter(getApplicationContext(), myProductList);
                // set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.setClickListener(dash.this::onItemClick);

                recyclerView.setAdapter(adapter);
            }
        });


    }

    public void onItemClick(View view, int position) {
        Log.e("onCLICKTEST", "onItemClick: " );
        Toast.makeText(this, "You clicked "  + " on row number " + position, Toast.LENGTH_SHORT).show();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dash, menu);
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
            case R.id.action_cart:
                startActivity(new Intent(dash.this, cart.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}