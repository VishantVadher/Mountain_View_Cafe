package com.example.mountainviewcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class cart extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mauth;

    public ArrayList<cartAdd> cartList;
    public double totalValue;
    cartAdapter adapter;

    Button buttonCheckOut;
    Toolbar toolbar;
    TextView totalAmountText;


    ArrayAdapter<QueryDocumentSnapshot> myarrayadapterlist;
    List<QueryDocumentSnapshot> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mFirestore = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        buttonCheckOut = (Button) findViewById(R.id.buttonCheckout);

//        toolbar = findViewById(R.id.address_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        final String appuid = FirebaseAuth.getInstance().getUid();
        mFirestore.collection("Cart").whereEqualTo("userID", appuid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(cart.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cartList = new ArrayList<>();
                totalValue = 0;

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    cartAdd product = doc.toObject(cartAdd.class);
                    product.setCartID(doc.getId());
                    totalValue = totalValue + Double.parseDouble(product.getPrice());
                    cartList.add(product);
                }
                adapter = new cartAdapter(getApplicationContext(), cartList, String.valueOf(totalValue));
                RecyclerView recyclerView = findViewById(R.id.recycler_cart);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);

                totalAmountText = (TextView) findViewById(R.id.totalAmountText);
                totalAmountText.setText("Total Amount : $" + String.valueOf(totalValue));
            }
        });


        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), checkout.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}