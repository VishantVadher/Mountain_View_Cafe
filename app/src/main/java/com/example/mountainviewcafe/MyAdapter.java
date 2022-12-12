package com.example.mountainviewcafe;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

//    private List<String> mData;
    private LayoutInflater mInflater;
    private Context adapterContext;
    private ItemClickListener mClickListener;
    public String productID;
    public String description;
    public String price;


//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    private List<addProduct> productList;

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
    public void onBindViewHolder(ViewHolder holder, int position) {


        final addProduct product = productList.get(position);

        holder.title.setText(product.getTitle());
//        holder.description.setText(product.getDescription());

        Log.e("IMAGEPICASSO",  product.getImage().toString() );

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(product.getImage()).into(holder.imageView);



        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("POS", String.valueOf(position) );

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