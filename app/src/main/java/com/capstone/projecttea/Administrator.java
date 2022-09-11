package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class Administrator extends AppCompatActivity {

    CardView cardHistory,cardManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        cardHistory = findViewById(R.id.cardHistory);
        cardManage = findViewById(R.id.cardAdd);
        ArrayList<ProductModel> productModels  =  new ArrayList<>();

        productModels  =  new ArrayList<>();
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));

        RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.adminPageRecyclerView);


        AdminOrderRecyclerView adapter = new AdminOrderRecyclerView(getApplicationContext(),productModels,AdminOrderRecyclerView.Page.main);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productRecyclerView.setAdapter(adapter);

        cardHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminHistory.class));
            }
        });
        cardManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Manage.class));
            }
        });

    }
}