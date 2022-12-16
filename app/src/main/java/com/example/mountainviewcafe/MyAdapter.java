package com.example.mountainviewcafe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //    private List<String> mData;
    private LayoutInflater mInflater;
    private Context adapterContext;
    private ItemClickListener mClickListener;
    public String productID;
    public String description;
    public String price;


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    private List<addProduct> productList;
    private List<cartAdd> cartList;

    // data is passed into the constructor
    MyAdapter(Context context, List<addProduct> addProducts) {
        this.mInflater = LayoutInflater.from(context);
//        this.mInflater = LayoutInflater.from(activity);
        this.productList = addProducts;
        Log.d("Adapter called", "MyAdapter : : : : ");
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, image, price, discount;
        public ConstraintLayout constraint;
        public ImageView imageView;
        public Button addToCart;


        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleProduct);
            imageView = itemView.findViewById(R.id.imageProduct);
            addToCart = itemView.findViewById(R.id.addToCart);
            constraint = itemView.findViewById(R.id.constraint);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final addProduct product = productList.get(position);

        holder.title.setText(product.getTitle());
//        holder.description.setText(product.getDescription());

        Log.e("IMAGEPICASSO",  product.getImage().toString() );

        Picasso.get().load(product.getImage()).into(holder.imageView);

//        Glide.with()
//                .load("https://images.unsplash.com/photo-1500100586562-f75ff6540087?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3589&q=80")
//                .transform(new CenterCrop(),new RoundedCorners(25))
//                .into(holder.imageView);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProduct.setSelected(!addProduct.isSelected());
                mAuth = FirebaseAuth.getInstance();
                mFirestore = FirebaseFirestore.getInstance();
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                cartList = new ArrayList<>();

                cartAdd cartAdd = new cartAdd(product.getId(),userid,"1", product.getTitle(), product.getDiscount(),
                        product.getImage(), product.getDescription(), product.getPrice());
//                holder.addToCart.setEnabled(false);

                mFirestore.collection("Cart").document().set(cartAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        holder.addToCart.setText("Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", "onFailure: " + e );
                    }
                });

            }
        });


        holder.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addProduct.setSelected(!addProduct.isSelected());
                Log.e("cardViewclick", "THIS : " + product.getId());
                productID = product.getId().toString();
                Intent intent = new Intent(holder.itemView.getContext(), detailView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", product.getId());
                holder.itemView.getContext().startActivity(intent);


//                adapterContext.startActivity(new Intent(adapterContext, detailView.class));
//                if(addProduct.isSelected()) {
//                    productID += product.getId().trim();
//                } else {
//                    productID = "";
//                }


            }
        });

//        holder.cardView.setBackgroundColor(product.isSelected() ? Color.DKGRAY : Color.GRAY);
//        holder.carbs.setText(food.getCar() + "");
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        Log.e( "getItem: ", String.valueOf(productList.get(id)) );
        return String.valueOf(productList.get(id));
    }

    @Override
    public int getItemCount() {
        Log.e("getitemcount", "getitemcountcalled");
        return productList.size();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}