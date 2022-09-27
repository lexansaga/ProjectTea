package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHistory extends AppCompatActivity {
    CardView cardHome,cardManage;
    ImageView userButton;
    FirebaseFirestore firestore;
    RecyclerView productRecyclerView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_admin_history);
        cardHome = findViewById(R.id.cardHistoryHome);
        cardManage = findViewById(R.id.cardHistoryManage);

        userButton = findViewById(R.id.adminHistoryUser);

        preferences = getSharedPreferences("User", MODE_PRIVATE);



         productRecyclerView = (RecyclerView)findViewById(R.id.historyPageRecyclerView);
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
    //   productModels.add(new ProductModel("1",new OrderModel("ABC123","99","120","20","40","Order Ready"),new UserModel("Alexander Saga","09126997937","Santa Rosa ")));
        SharedPreferences  preferences = getSharedPreferences("User", MODE_PRIVATE);
        String user =   preferences.getString("User","");
        String accountType =   preferences.getString("AccountType","");
        Query collection;
      if(!accountType.equals("Administrator")){
          //User Account
          collection = firestore.collection("User").whereEqualTo("Email",user);
          cardHome.setVisibility(View.GONE);
          cardManage.setVisibility(View.GONE);
         // Toast.makeText(getApplicationContext(),"User "+ user, Toast.LENGTH_SHORT).show();
          collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
              @Override
              public void onEvent(@Nullable QuerySnapshot userSnapshots, @Nullable FirebaseFirestoreException e) {

                  if(userSnapshots.isEmpty()){
                      //      Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                      return;
                  }
                  for(QueryDocumentSnapshot userSnapshot : userSnapshots){
                      // In this block of code, we will get the data from the User Collection in which the Email is same with the user to get its data
                      String userEmail = userSnapshot.get("Email").toString();
                      String userName = userSnapshot.get("Fullname").toString();
                      String userContact = userSnapshot.get("Contact").toString();
                      String userAddress = userSnapshot.get("Address").toString();
                      Query orders = firestore.collection("Orders").document(userEmail).collection("Orders");
                      orders.addSnapshotListener(new EventListener<QuerySnapshot>() {
                          @Override
                          public void onEvent(@Nullable QuerySnapshot orderSnapshots, @Nullable FirebaseFirestoreException e) {
                              ArrayList<ProductModel>   productModels =  new ArrayList<>();
                              if(orderSnapshots.isEmpty()){
                                  //Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                                  return;
                              }
                              for(QueryDocumentSnapshot orderSnapshot : orderSnapshots){
                                  //Then we will get the data one by one.
                                  String orderID = orderSnapshot.get("ID").toString();
                             //     Toast.makeText(getApplicationContext(),orderID, Toast.LENGTH_SHORT).show();
                                  String orderUserID = orderSnapshot.get("UserID").toString();
                                  String orderNo = orderSnapshot.get("OrderNo").toString();
                                  String timeOrder = orderSnapshot.get("TimeOrder").toString();
                                  String status = orderSnapshot.get("Status").toString();
                                  String total = orderSnapshot.get("Total").toString();
                                  if(!status.contains("InProgress")){
                                      //Then we will append the data on ArrrayList of ProductModels if the data was not In Progress
                                      productModels.add(new ProductModel(orderID,new OrderModel(orderID,orderNo,"0","0",total,status),new UserModel(userName,userContact,userAddress,userEmail)));
                                  }

                              }
                              // Lastly, we pass it on recycler view to display it on our view

                              AdminOrderRecyclerView adapter = new AdminOrderRecyclerView(getApplicationContext(),productModels,AdminOrderRecyclerView.Page.history);
                              productRecyclerView.setHasFixedSize(true);
                              productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                              productRecyclerView.setAdapter(adapter);
                              adapter.notifyDataSetChanged();
                          //      tinyDB.putListString();
                          }
                      });

                      //Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_SHORT).show();
                  }


              }
          });

      }
      else{
          //Admin Account Account

          collection = firestore.collection("User");
        //  Toast.makeText(getApplicationContext(), "Administrator", Toast.LENGTH_SHORT).show();

          ArrayList<ProductModel>   productModels =  new ArrayList<>();
          collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
              // First we will on all users on the database
              @Override
              public void onEvent(@Nullable QuerySnapshot userSnapshots, @Nullable FirebaseFirestoreException e) {

                  if(userSnapshots.isEmpty()){
                      //      Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                      return;
                  }
                  for(QueryDocumentSnapshot userSnapshot : userSnapshots){
                      String userEmail = userSnapshot.get("Email").toString();
                      String userName = userSnapshot.get("Fullname").toString();
                      String userContact = userSnapshot.get("Contact").toString();
                      String userAddress = userSnapshot.get("Address").toString();
                      Query orders = firestore.collection("Orders").document(userEmail).collection("Orders");
                     //Then we pass the emails to the query so we get the specific data
                      orders.addSnapshotListener(new EventListener<QuerySnapshot>() {
                          @Override
                          public void onEvent(@Nullable QuerySnapshot orderSnapshots, @Nullable FirebaseFirestoreException e) {

                              if(orderSnapshots.isEmpty()){
                                  //Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                                  return;
                              }
                              for(QueryDocumentSnapshot orderSnapshot : orderSnapshots){
                                  // Then we loop on the snapshots to get the data individually and add it to the container
                                  String orderID = orderSnapshot.get("ID").toString();
                                  String orderUserID = orderSnapshot.get("UserID").toString();
                                  String orderNo = orderSnapshot.get("OrderNo").toString();
                                  String timeOrder = orderSnapshot.get("TimeOrder").toString();
                                  String status = orderSnapshot.get("Status").toString();
                                  String total = orderSnapshot.get("Total").toString();
                                  if(!status.contains("InProgress")){
                                      Log.e("AdminHistoryOrderID",orderID);
                                      //Let's add the data if the status of the data not InProgress
                                      productModels.add(new ProductModel(orderID,new OrderModel(orderID,orderNo,"0","0",total,status),new UserModel(userName,userContact,userAddress,userEmail)));
                                  }

                              }
                              //Lastly we will append the data on the adapter to get its view
                              AdminOrderRecyclerView adapter = new AdminOrderRecyclerView(getApplicationContext(),productModels,AdminOrderRecyclerView.Page.history);
                              productRecyclerView.setHasFixedSize(true);
                              productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                              productRecyclerView.setAdapter(adapter);
                              adapter.notifyDataSetChanged();

                          }
                      });
                      //Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_SHORT).show();
                  }


              }
          });

      }

        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Administrator.class));
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.user_information:
                                menuItem.setVisible(false);
                                startActivity(new Intent(getApplicationContext(), User.class));
                                return true;
                            case R.id.user_history:
                                startActivity(new Intent(getApplicationContext(), AdminHistory.class));
                                menuItem.setVisible(true);
                                return true;
                            case R.id.user_logout:
                                if(!accountType.equals("Administrator")){
                                    preferences.edit().remove("User").apply();
                                }
                                else{
                                    preferences.edit().remove("AccountType").apply();
                                }

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