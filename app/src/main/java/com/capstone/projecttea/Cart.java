package com.capstone.projecttea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    Button btnCartCheckout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnCartCheckout = findViewById(R.id.btncartCheckout);

        productModels  =  new ArrayList<>();
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.cartRecyclerView);


        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Cart,productModels,getApplicationContext());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(adapter);

        btnCartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CheckoutPage.class));
            }
        });
    }
}