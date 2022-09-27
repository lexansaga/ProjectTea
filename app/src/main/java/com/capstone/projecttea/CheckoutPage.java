package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPage extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    Button btnCheckout;
    ImageView btnCart,btnUser,btnEditUser;
    TextView txtName,txtContact,txtAddress,txtSubTotal,txtDeliveryFee,txtTotal;

    FirebaseFirestore firestore;
    String user,buyNow;
    final String currencySign = "₱";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);

        firestore = FirebaseFirestore.getInstance();

        btnCart = findViewById(R.id.checkoutCart);
        btnUser = findViewById(R.id.checkoutUser);
        btnEditUser = findViewById(R.id.btnCheckoutEditUser);
        btnCheckout = findViewById(R.id.btnProceedCheckout);

        txtName = findViewById(R.id.txtCheckoutName);
        txtContact = findViewById(R.id.txtCheckoutContact);
        txtAddress = findViewById(R.id.txtCheckoutAddress);
        txtSubTotal = findViewById(R.id.txtCheckoutSubtotal);
        txtDeliveryFee = findViewById(R.id.txtCheckoutDeliveryFee);
        txtTotal = findViewById(R.id.txtCheckoutTotal);

    Bundle extras = getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences("User",MODE_PRIVATE);
    if(extras != null){
        buyNow = Utils.CheckTextIfNull(extras.get("isBuyNow"),"false");
        if(buyNow.contains("true")){
            productModels  =  (ArrayList<ProductModel>) getIntent().getSerializableExtra("cart");
        }
        else{
            productModels  =  (ArrayList<ProductModel>) getIntent().getSerializableExtra("checkout");
        }
        user = preferences.getString("User","").toString();
    }


        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.checkOutRecyclerView);


        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout,productModels,getApplicationContext());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(adapter);


        Query getUserInfo = firestore.collection("User").whereEqualTo("Email",user);
        getUserInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                   for(QueryDocumentSnapshot snapshot : snapshots){
                       String userName = snapshot.get("Fullname").toString().trim();
                   //    String userContact = snapshot.get("Contact").toString().trim();
                  //     String userAddress = snapshot.get("Address").toString().trim();
                       String userEmail = snapshot.get("Email").toString().trim();
                       txtName.setText(userName);
                    //   Toast.makeText(CheckoutPage.this, ""+userContact, Toast.LENGTH_SHORT).show();
                   //    Toast.makeText(getApplicationContext(), ""+userName, Toast.LENGTH_SHORT).show();
                  //     txtContact.setText(userContact);
                   //    txtAddress.setText(userAddress);
                   }

           }
       });
        ArrayList<ProductModel> models = new ArrayList<>();
        double subTotal = 0;
        for(int i = 0; i < adapter.getItemCount();i++){

                String id = productModels.get(i).getID(); // Cart ID
                String productId = productModels.get(i).getImageLink();
                String name = productModels.get(i).getProductName();
                Double price = Double.parseDouble(productModels.get(i).getPrice());
                String variation = productModels.get(i).getVariation();
                String addons = productModels.get(i).getAddOns();
                int quantity = productModels.get(i).getQuantity();
                subTotal += (quantity * price);

                ProductModel productModel = new ProductModel();
                productModel.setID(id);
                productModel.setImageLink(productId);
                productModel.setProductName(name);
                productModel.setPrice(String.valueOf(price));
                productModel.setVariation(variation);
                productModel.setQuantity(quantity);
                productModel.setAddOns(addons);

                models.add(productModel);
          //  models.add(new ProductModel(id,  productId, name, String.valueOf(price), productId.split("|")[1], quantity));


        }
        txtSubTotal.setText(currencySign+subTotal);
        txtDeliveryFee.setText("20");
        txtTotal.setText(txtSubTotal.getText().toString());

       btnEditUser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),User.class));
           }
       });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.user_information:
                                startActivity(new Intent(getApplicationContext(),User.class));
                                return true;
                            case R.id.user_history:
                                startActivity(new Intent(getApplicationContext(),AdminHistory.class));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_user, popup.getMenu());
                popup.show();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Cart.class));
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initiate data and put it on an HashMap before inserting on the database
                String id = firestore.collection("Orders").document(user).collection("Orders").document().getId();
                String timeanddatenow = Utils.TimeAndDateNow("");
                String timeanddatenowName = Utils.TimeAndDateNow("datename");
                Map<String, Object> data = new HashMap<>();
                data.put("ID",id);
                data.put("UserID",user);
                data.put("Total",txtTotal.getText().toString().trim());
                data.put("TimeOrder",timeanddatenow);
                data.put("Status","InProgress");
                data.put("OrderNo",Utils.TimeAndDateNow("time"));
                data.put("TimeOrderName",timeanddatenowName);
                //***********************************************************************


                firestore.collection("Orders").document(user).collection("Orders").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       //Loop on Product Model array for Mass insert on Order Collection on Database
                        double total = 0;
                        for (ProductModel productModel: models) {
                            String uid = firestore.collection("Orders").document(user).collection("Product").document().getId();
                            String cId = productModel.getID(); // Cart ID
                            String productId = productModel.getImageLink();
                            String name = productModel.getProductName();
                            Double price = Double.parseDouble(productModel.getPrice());
                            String variation = productModel.getVariation();
                            String addons = productModel.getAddOns();
                            int quantity = productModel.getQuantity();
                            double itemPrice = price * quantity;
                            total += (quantity * price);
                            Map<String, Object> productData = new HashMap<>();
                            productData.put("ID",uid);
                            productData.put("OrderID",id);
                            productData.put("ProductID",productModel.getID());
                            productData.put("ImageLink",productModel.getImageLink());
                            productData.put("Name",name);
                            productData.put("Quantity",quantity);
                            productData.put("Variation",variation);
                            productData.put("Price",price);
                            productData.put("Total_Price",itemPrice);
                            productData.put("TimeOrder",timeanddatenow);
                            productData.put("AddOns",addons);
                            //************************************************************


                            //After checking out delete the item on cart
                            firestore.collection("Cart").document(cId).delete();
                            //*****************************************

                            // Add the data now on Orders Collection
                            firestore.collection("Orders").document(user).collection("Product").document(uid).set(productData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Order Submitted Sucessfully!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            // ***************************************
                        }
                    }
                });
                startActivity(new Intent(getApplicationContext(),CheckoutSuccess.class));
            }
        });
    }
}