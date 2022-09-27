package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

import io.opencensus.internal.StringUtils;

public class Home extends AppCompatActivity {
    ArrayList<ProductModel> productModels;
    ImageView cart, user;
    EditText edtSearchTea;
    SharedPreferences preferences;
    FirebaseFirestore firestore;
    StorageReference storageReference;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cart = findViewById(R.id.homeCart);
        user = findViewById(R.id.homeUser);

        edtSearchTea = findViewById(R.id.edtSearchTea);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        tabLayout = findViewById(R.id.homeTabLayout);

      //  Utils.ShowDialog(Home.this,"Project!","This project is created by Alexander Saga!");

        Query tabNavQuery = firestore.collection("Items").whereEqualTo("Series", "");


        Query allQuery = firestore.collection("Items");
        LoadHomeProduct(allQuery); //Load Products Onload
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        firestore.collection("Series").addSnapshotListener(new EventListener<QuerySnapshot>() {
            //Adding Series Tab Nav
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot.isEmpty()){
                    return;
                }

                for (QueryDocumentSnapshot data: snapshot
                     ) {
                    String seriesName = data.get("Name").toString();
                    tabLayout.addTab(tabLayout.newTab().setText(seriesName));
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0){
                    Query allQuery = firestore.collection("Items");
                    LoadHomeProduct(allQuery);
                }
                else
                {
                    String tabName = tab.getText().toString();
                    Query query = firestore.collection("Items").whereEqualTo("Series",tabName);
                    LoadHomeProduct(query);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        edtSearchTea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().isEmpty()){
                   // Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                    LoadHomeProduct(allQuery);
                }
                else{
                 //   Toast.makeText(getApplicationContext(), ""+charSequence, Toast.LENGTH_SHORT).show();
                    Query searchQuery = firestore.collection("Items").orderBy("Name").startAt(Utils.CapitalizeString(getApplicationContext(),charSequence.toString())).endAt(Utils.CapitalizeString(getApplicationContext(),charSequence.toString()) + '\uf8ff');
                    LoadHomeProduct(searchQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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


    void LoadHomeProduct(Query query){

        //This function will scan the documents base on the query given and append it to array adapter which it displays the layout from the data.
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                productModels = new ArrayList<>();
                ArrayList<String> itemNameList = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : snapshots
                ) {
                    String imageLink = snapshot.get("ImageLink") != null ? snapshot.get("ImageLink").toString() : "";
                    String itemName = snapshot.get("Name").toString();

                    if(itemNameList.indexOf(itemName) < 0){
                        //Toast.makeText(Home.this, "Product Exists!", Toast.LENGTH_SHORT).show();
                        String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";
                        productModels.add(new ProductModel(snapshot.get("ID").toString(), imageLink.equals("") ? defaultImageLink : imageLink, itemName, snapshot.get("Price").toString()));
                        itemNameList.add(itemName);
                    }
                    else{

                    }

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
    }

}