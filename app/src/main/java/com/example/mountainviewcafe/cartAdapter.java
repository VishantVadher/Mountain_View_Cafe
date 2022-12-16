package com.example.mountainviewcafe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {

    //    private List<String> mData;
    private LayoutInflater mInflater;
    private Context adapterContext;
    private ItemClickListener mClickListener;
    public String productID;
    public String description;
    public String price;
    public String cartTotalPrice;


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private List<cartAdd> cartList;


    cartAdapter(Context context, List<cartAdd> cartAdd, String cartTotalPrice) {
        this.mInflater = LayoutInflater.from(context);
        this.cartList = cartAdd;
        this.cartTotalPrice = cartTotalPrice;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, image, price, discount, totalAmountText;
        public ConstraintLayout constraint;
        public ImageView imageView;
        public Button addToCart;
        public Button removeFromCart;

        ViewHolder(View itemView) {
            super(itemView);
            mFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            title = itemView.findViewById(R.id.productTitleCart);
            imageView = itemView.findViewById(R.id.productImage);
            price = itemView.findViewById(R.id.productPriceCart);
            totalAmountText = itemView.findViewById(R.id.totalAmountText);

            removeFromCart = itemView.findViewById(R.id.removeCart);

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
        View view = mInflater.inflate(R.layout.listviewcart, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final cartAdd cartProduct = cartList.get(position);
        holder.title.setText(cartProduct.getTitle());
        holder.price.setText(cartProduct.getPrice());
//        holder.totalAmountText.setText(cartTotalPrice);

        Picasso.get().load(cartProduct.getImage()).into(holder.imageView);

        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String appuid = FirebaseAuth.getInstance().getUid();

                Log.e("CARTID", cartProduct.getCartID() );

                mFirestore.collection("Cart").document(cartProduct.getCartID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Deleted", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERRORINDELETE", "Error deleting document", e);
                            }
                        });
            }
        });


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        Log.e( "getItem: ", String.valueOf(cartList.get(id)) );
        return String.valueOf(cartList.get(id));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
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