package com.capstone.projecttea;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    public ProductRecyclerViewAdapter(ArrayList<ProductModel> productModels, Context context){
        productModelArrayList = productModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =  LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_items, viewGroup, false);
        ViewHolder viewHolder =  new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String currency = "₱";
    ProductModel productModel = productModelArrayList.get(position);
    if(productModel.getProductImage() == 0){
        Glide.with(context).load(productModel.getImageLink()).into(viewHolder.productImage);
    }
    else {

        viewHolder.productImage.setImageResource(productModel.getProductImage());
    }
    viewHolder.productName.setText(productModel.getProductName());
    viewHolder.productPrice.setText(currency+productModel.getPrice());

    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,ProductDescription.class);
            intent.putExtra("productID",productModel.getID());
            view.getContext().startActivity(intent);
        }
    });

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName,productPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productImage = (ImageView)itemView.findViewById(R.id.productImage);
            this.productName = (TextView)itemView.findViewById(R.id.productName);
            this.productPrice = (TextView) itemView.findViewById(R.id.productPrice);
        }
    }
}
