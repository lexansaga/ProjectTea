package com.capstone.projecttea;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

  public enum CartVariation{Cart,Checkout}


    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    CartVariation cartVariation;
    public CartRecyclerViewAdapter(CartVariation cartVariation,ArrayList<ProductModel> productModels, Context context){
        this.cartVariation = cartVariation;
        productModelArrayList = productModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =  LayoutInflater.from(viewGroup.getContext());
        View listItem;
        if(cartVariation == CartVariation.Cart){
            listItem = layoutInflater.inflate(R.layout.layout_cart, viewGroup, false);
        }
        else{
            listItem = layoutInflater.inflate(R.layout.layout_checkoutcart, viewGroup, false);
        }

        ViewHolder viewHolder =  new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
       final String currencySign ="₱ ";
        ProductModel productModel = productModelArrayList.get(position);

        viewHolder.productImage.setImageResource(productModel.getProductImage());
        viewHolder.productName.setText(productModel.getProductName());
        viewHolder.productPrice.setText(currencySign+productModel.getPrice());
        String[] variations = productModel.getVariations();

        viewHolder.productVariations.setText("Variations: "+String.join(",",variations));
        viewHolder.quantity.setText(productModel.getQuantity()+"");


        if(cartVariation == CartVariation.Cart){
            viewHolder.productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.quantityPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantityVal = viewHolder.quantity.getText().toString();
                    if (!quantityVal.isEmpty()) {
                        int quantityValue = Integer.parseInt(quantityVal);
                        if (quantityValue < 999) {
                            quantityValue++;
                            viewHolder.quantity.setText("" + quantityValue);
                            //  Toast.makeText(getApplicationContext(), "Plus " + quantityValue, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            viewHolder.quantityMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String quantityVal = viewHolder.quantity.getText().toString();
                    if (!quantityVal.isEmpty()) {
                        int quantityValue = Integer.parseInt(quantityVal);
                        if (quantityValue > 0) {
                            quantityValue--;
                            viewHolder.quantity.setText("" + quantityValue);
                        }
                    }
                    // Toast.makeText(getApplicationContext(), "Minus", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String quantityVal = viewHolder.quantity.getText().toString();
                    if (!quantityVal.isEmpty()) {
                        int quantityValue = Integer.parseInt(quantityVal);
                        if (quantityValue < 0 || quantityValue > 999) {
                            viewHolder.quantity.setText("" + 0);
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        else{

        }

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName,productPrice,productVariations;
        public ImageButton productDelete,quantityPlus,quantityMinus;
        public TextView quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if(cartVariation == CartVariation.Cart){
                this.productImage = (ImageView)itemView.findViewById(R.id.cartProductImage);
                this.productName = (TextView)itemView.findViewById(R.id.cartProductName);
                this.productVariations = (TextView)itemView.findViewById(R.id.cartVariationsValue);
                this.productPrice = (TextView) itemView.findViewById(R.id.cartPrice);

                this.productDelete = (ImageButton) itemView.findViewById(R.id.cartDelete);
                this.quantity = (EditText) itemView.findViewById(R.id.cartQuantityValue);
                this.quantityPlus = (ImageButton) itemView.findViewById(R.id.cartQuantityPlus);
                this.quantityMinus = (ImageButton) itemView.findViewById(R.id.cartQuantityMinus);
            }
            else{
                this.productImage = (ImageView)itemView.findViewById(R.id.checkCartProductImage);
                this.productName = (TextView)itemView.findViewById(R.id.checkCartProductName);
                this.productVariations = (TextView)itemView.findViewById(R.id.checkCartVariationsValue);
                this.productPrice = (TextView) itemView.findViewById(R.id.checkCartPrice);
                this.quantity = (TextView) itemView.findViewById(R.id.checkCartQuantity);
            }

        }
    }
}
