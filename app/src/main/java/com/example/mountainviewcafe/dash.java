package com.example.mountainviewcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class dash extends AppCompatActivity {


    MyAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mauth;
    ArrayList<addProduct> myProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mFirestore = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        final String appuid = FirebaseAuth.getInstance().getUid();
        mFirestore.collection("products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(dash.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("SIZEE", "onEvent: " + queryDocumentSnapshots.size());

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    addProduct product = doc.toObject(addProduct.class);
                    myProductList.add(product);
                    Log.e("DOC", doc.get("title").toString() );
                    Log.e("myProductList", doc.toString() );

                }
            }
        });


        adapter = new MyAdapter(this, myProductList);
        RecyclerView recyclerView = findViewById(R.id.rView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter.setClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);

//        adapter = new MyAdapter(this, myProductList);
//        // set up the RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.rView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
////        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);

    }

    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}