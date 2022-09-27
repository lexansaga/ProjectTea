package com.capstone.projecttea;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    public enum CartVariation {
        Cart,
        Checkout,
        Admin
    }

    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    CartVariation cartVariation;
    FirebaseFirestore firestore;
    ViewHolder viewHolder;

    public CartRecyclerViewAdapter(CartVariation cartVariation, ArrayList<ProductModel> productModels, Context context) {
        this.cartVariation = cartVariation;
        productModelArrayList = productModels;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem;
        if (cartVariation == CartVariation.Cart) {
            listItem = layoutInflater.inflate(R.layout.layout_cart, viewGroup, false);
        } else {
            listItem = layoutInflater.inflate(R.layout.layout_checkoutcart, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        this.viewHolder = viewHolder;
        final String currencySign = "₱";
        ProductModel productModel = productModelArrayList.get(position);



        if (productModel.getProductImage() == 0) {
            StorageReference reference = FirebaseStorage.getInstance().getReference();
            Log.e("Image Link",productModel.getID());
            reference.child("Items/" + productModel.getImageLink()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri.toString()).into(viewHolder.productImage);
                   // Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Image Error",e.toString());
                    String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";

                    Glide.with(context).load(defaultImageLink).into(viewHolder.productImage);
                }
            });

        } else {

            viewHolder.productImage.setImageResource(productModel.getProductImage());
        }
        viewHolder.productName.setText(productModel.getProductName());
        viewHolder.productPrice.setText(currencySign + Double.parseDouble(productModel.getPrice()) * productModel.getQuantity());
        //  String[] variations = productModel.getVariations();
        //  if (variations != null) {
        //      viewHolder.productVariations.setText("Variations: " + String.join(",", variations));
        //
        //  }
  //      Toast.makeText(context.getApplicationContext(), productModel.getVariation(), Toast.LENGTH_SHORT).show();
        viewHolder.productVariations.setText("Variations: " + productModel.getVariation());
        viewHolder.quantity.setText(productModel.getQuantity() + "");
        if (cartVariation == CartVariation.Cart) {
            if(productModel.getAddOns().equals("")){
                viewHolder.productAddOns.setVisibility(View.GONE);
            }
            else{
                viewHolder.productAddOns.setText("AddOns \n"+productModel.getAddOns().replaceAll(":"," +").replaceAll(",","\n"));
            }

            viewHolder.chkBoxCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    productModel.setChecked(isChecked);
                }
            });
            viewHolder.productDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    Toast.makeText(context, productModel.getID(), Toast.LENGTH_SHORT).show();

                    firestore.collection("Cart").document(productModel.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Item deleted from Cart", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        } else if (cartVariation == CartVariation.Admin) {
            viewHolder.quantity.setText(productModel.getQuantity());
        } else if (cartVariation == CartVariation.Checkout) {
            if(productModel.getAddOns().equals("")){
                viewHolder.productAddOns.setVisibility(View.GONE);
            }
            else{
                viewHolder.productAddOns.setText("AddOns \n"+productModel.getAddOns().replaceAll(":"," +").replaceAll(",","\n"));
            }
           // Toast.makeText(context, productModel.getAddOns(), Toast.LENGTH_SHORT).show();
            String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";
            String imageLink = productModel.getImageLink();
            Glide.with(context).load(imageLink.equals("") ? defaultImageLink : imageLink).into(viewHolder.productImage);


        }

    }

    public ArrayList<ProductModel> getProductModelArrayList() {
        return productModelArrayList;
    }

    public boolean isItemChecked() {
        return viewHolder.chkBoxCart.isChecked();
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice, productVariations, productAddOns;
        public ImageButton productDelete, quantityPlus, quantityMinus;
        public TextView quantity;
        public CheckBox chkBoxCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (cartVariation == CartVariation.Cart) {
                this.productImage = (ImageView) itemView.findViewById(R.id.cartProductImage);
                this.productName = (TextView) itemView.findViewById(R.id.cartProductName);
                this.productVariations = (TextView) itemView.findViewById(R.id.cartVariationsValue);
                this.productPrice = (TextView) itemView.findViewById(R.id.cartPrice);
                this.productAddOns = (TextView) itemView.findViewById(R.id.cartAddOns);

                this.productDelete = (ImageButton) itemView.findViewById(R.id.cartDelete);
                this.quantity = (EditText) itemView.findViewById(R.id.cartQuantityValue);
                this.quantityPlus = (ImageButton) itemView.findViewById(R.id.cartQuantityPlus);
                this.quantityMinus = (ImageButton) itemView.findViewById(R.id.cartQuantityMinus);

                this.chkBoxCart = (CheckBox) itemView.findViewById(R.id.chkCart);
            } else {
                this.productImage = (ImageView) itemView.findViewById(R.id.checkCartProductImage);
                this.productName = (TextView) itemView.findViewById(R.id.checkCartProductName);
                this.productVariations = (TextView) itemView.findViewById(R.id.checkCartVariationsValue);
                this.productPrice = (TextView) itemView.findViewById(R.id.checkCartPrice);
                this.quantity = (TextView) itemView.findViewById(R.id.checkCartQuantity);
                this.productAddOns = (TextView) itemView.findViewById(R.id.checkCartAddOnsValue);
            }

        }
    }
}