package com.example.mountainviewcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.RoundedCorner;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class dash extends AppCompatActivity {


    MyAdapter adapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mauth;
    public ArrayList<addProduct> myProductList;
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
//                .load(")
//                .transform(new RoundedCornersTransform()).into(imageView);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1500100586562-f75ff6540087?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3589&q=80")
                .transform(new CenterCrop(),new RoundedCorners(25))
                .into(imageView);



        final String appuid = FirebaseAuth.getInstance().getUid();
        mFirestore.collection("products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(dash.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("SIZEE", "onEvent: " + queryDocumentSnapshots.size() );

                myProductList = new ArrayList<>();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    addProduct product = doc.toObject(addProduct.class);
                    product.setId(doc.getId());
                    myProductList.add(product);
                    Log.e("myProductList", doc.toString() );
                    Log.e("document id ", doc.getId()  );

                }

                adapter = new MyAdapter(getApplicationContext(), myProductList);
                // set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rView);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                adapter.setClickListener(dash.this::onItemClick);
                recyclerView.setAdapter(adapter);
            }
        });


    }

    public void onItemClick(View view, int position) {
        Log.e("onCLICKTEST", "onItemClick: " );
//        Toast.makeText(this, "You clicked "  + " on row number " + position, Toast.LENGTH_SHORT).show();

        String prodID =  adapter.productID;
        Toast.makeText(this, "You clicked "  + prodID, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, detailView.class);
        intent.putExtra("id", prodID);
        startActivity(intent);
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