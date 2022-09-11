package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class AdminHistory extends AppCompatActivity {
    CardView cardHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history);
        cardHome = findViewById(R.id.cardHistoryHome);
        ArrayList<ProductModel> productModels  =  new ArrayList<>();

        productModels  =  new ArrayList<>();
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.historyPageRecyclerView);


        AdminOrderRecyclerView adapter = new AdminOrderRecyclerView(getApplicationContext(),productModels,AdminOrderRecyclerView.Page.history);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productRecyclerView.setAdapter(adapter);

        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Administrator.class));
            }
        });
    }
}