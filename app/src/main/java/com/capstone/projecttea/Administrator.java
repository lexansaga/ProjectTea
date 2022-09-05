package com.capstone.projecttea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Administrator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        ArrayList<ProductModel> productModels  =  new ArrayList<>();

        productModels  =  new ArrayList<>();
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.adminPageRecyclerView);


        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout,productModels,getApplicationContext());
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productRecyclerView.setAdapter(adapter);

    }
}