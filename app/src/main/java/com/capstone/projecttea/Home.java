package com.capstone.projecttea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    ImageView cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cart = findViewById(R.id.homeCart);

        productModels  =  new ArrayList<>();
        productModels.add(new ProductModel(R.drawable.product,"Honey Tea","59.45"));
        productModels.add(new ProductModel(R.drawable.product,"Honey Tea","59.45"));
        productModels.add(new ProductModel(R.drawable.product,"Honey Tea","59.45"));
        productModels.add(new ProductModel(R.drawable.product,"Honey Tea","59.45"));

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.cartRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(productModels,getApplicationContext());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(mLayoutManager);
        productRecyclerView.setAdapter(adapter);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Cart.class));
            }
        });
    }
}