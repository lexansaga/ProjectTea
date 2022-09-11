package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    ImageView cart, user;
    SharedPreferences preferences;
    FirebaseFirestore firestore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cart = findViewById(R.id.homeCart);
        user = findViewById(R.id.homeUser);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Query tabNavQuery = firestore.collection("Items").whereEqualTo("Series", "");
        Query allQuery = firestore.collection("Items");
        allQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                productModels = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : snapshots
                ) {
                    String imageLink = snapshot.get("ImageLink").toString();
                    String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";
                    productModels.add(new ProductModel(snapshot.get("ID").toString(), imageLink.equals("") ? defaultImageLink : imageLink, snapshot.get("Name").toString(), snapshot.get("Price").toString()));
                }


                RecyclerView productRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(productModels, getApplicationContext());
                productRecyclerView.setHasFixedSize(true);
                productRecyclerView.setLayoutManager(mLayoutManager);
                productRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        preferences = getSharedPreferences("User", MODE_PRIVATE);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
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
                                preferences.edit().remove("User").apply();
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