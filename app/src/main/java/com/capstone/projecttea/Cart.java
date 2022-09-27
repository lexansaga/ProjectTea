package com.capstone.projecttea;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class Cart extends AppCompatActivity {

    Button btnCartCheckout;
    ImageView btnCart, btnUser;
    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;
    RecyclerView productRecyclerView;
    CartRecyclerViewAdapter adapter;
    ArrayList<ProductModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        firestore = FirebaseFirestore.getInstance();

        btnCartCheckout = findViewById(R.id.btncartCheckout);
        btnCart = findViewById(R.id.cartCart);
        btnUser = findViewById(R.id.cartUser);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("User", "");


        productRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);

        Query getCart = firestore.collection("Cart").whereEqualTo("UserID", currentUser);
        getCart.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapShots, @Nullable FirebaseFirestoreException e) {
                productModels = new ArrayList<>();



                for (QueryDocumentSnapshot snapShot : snapShots
                ) {
                    String id = snapShot.get("ID").toString().trim();
                    String productID = snapShot.get("ProductID").toString().trim();
                    String userID = snapShot.get("UserID").toString().trim();
                    String name = snapShot.get("ProductName").toString().trim();
                    String price = snapShot.get("ProductPrice").toString().trim();
                    String addOns = snapShot.get("AddOns").toString().trim();
                    String variation = Utils.CheckTextIfNull(snapShot.get("Variation"),"No Variation");
                    int quantity = Integer.parseInt(snapShot.get("Quantity").toString().trim());
                    //  String imageLink = snapShot.get("ImageLink").toString().trim();
                    String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";

                    ProductModel model = new ProductModel();
                    model.setID(id);
                    model.setImageLink(productID);
                    model.setProductName(name);
                    model.setPrice(price);
                    model.setVariation(variation);
                    model.setQuantity(quantity);
                    model.setAddOns(addOns);
                    // productModels.add(new ProductModel(id,  productID, name, price, productID.split("|")[1], quantity));
                    productModels.add(model);
                }
                adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Cart, productModels, getApplicationContext());
                productRecyclerView.setHasFixedSize(true);
                productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                productRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
        });


        btnCartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ProductModel> models = new ArrayList<>();

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (productModels.get(i).isChecked()) {
                        String id = productModels.get(i).getID(); // Cart ID
                        String productId = productModels.get(i).getImageLink();
                        String name = productModels.get(i).getProductName();
                        String price = productModels.get(i).getPrice();
                        String variation = productModels.get(i).getVariation();
                        String addOns = productModels.get(i).getAddOns();
                        int quantity = productModels.get(i).getQuantity();

                        ProductModel model = new ProductModel();
                        model.setID(id);
                        model.setImageLink(productId);
                        model.setProductName(name);
                        model.setPrice(price);
                        model.setVariation(variation);
                        model.setQuantity(quantity);
                        model.setAddOns(addOns);
                        models.add(model);
                        //  models.add(new ProductModel(id, productId, name, price, variation, quantity));
                        //   Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                    }

                    //       Toast.makeText(Cart.this, ""+productModels.get(i).isChecked(), Toast.LENGTH_SHORT).show();
                }
                if(models.size() <= 0 || adapter.getItemCount() <= 0){
                    Toast.makeText(getApplicationContext(), "Please select item!", Toast.LENGTH_SHORT).show();
                        return
                                ;
                }
                Intent intent = new Intent(getApplicationContext(), CheckoutPage.class);
                intent.putExtra("checkout", models);
                startActivity(intent);

            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
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
                                startActivity(new Intent(getApplicationContext(), User.class));
                                return true;
                            case R.id.user_history:
                                startActivity(new Intent(getApplicationContext(), AdminHistory.class));
                                return true;
                            case R.id.user_logout:
                                sharedPreferences.edit().remove("User").apply();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
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
    }
}