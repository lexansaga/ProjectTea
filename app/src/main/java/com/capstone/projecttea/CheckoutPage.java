package com.capstone.projecttea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CheckoutPage extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);


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

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.checkOutRecyclerView);


        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout,productModels,getApplicationContext());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(adapter);
    }
}