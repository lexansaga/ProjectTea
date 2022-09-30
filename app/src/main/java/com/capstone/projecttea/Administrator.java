package com.capstone.projecttea;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Timer;
import java.util.TimerTask;

public class Administrator extends AppCompatActivity {

    CardView cardHistory,cardManage;
    FirebaseFirestore firestore;
    ImageView user;

    SharedPreferences preferences;
    boolean onDataChanged = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        firestore = FirebaseFirestore.getInstance();

        cardHistory = findViewById(R.id.cardHistory);
        cardManage = findViewById(R.id.cardAdd);

        user = findViewById(R.id.adminUser);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        GenerateAdminOrders();

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

        user.setOnClickListener(new View.OnClickListener() {
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
                                return true;
                            case R.id.user_logout:
                                preferences.edit().remove("AccountType").apply();
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

    private void GenerateAdminOrders(){
        //In this block of code, will generate the Orders of the customer and show it to the Administrator Page
        Query collection = firestore.collection("User");
        ArrayList<ProductModel> productModels  =  new ArrayList<>();
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot userSnapshots, @Nullable FirebaseFirestoreException e) {

                if(userSnapshots.isEmpty()){
                    //      Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(onDataChanged == true){
                        productModels.clear();
                        onDataChanged = false;
                }
                for(QueryDocumentSnapshot userSnapshot : userSnapshots){
                    //In this block of code, will get the Information of the Users which will be needed later
                    String userEmail = userSnapshot.get("Email").toString();
                    String userName = userSnapshot.get("Fullname").toString();
                    Log.e("OrderNames",userName);
                    Log.e("OrderEmail",userEmail);
                    String userContact =Utils.CheckTextIfNull(userSnapshot.get("Contact"),"No Contact");
                    String userAddress = Utils.CheckTextIfNull(userSnapshot.get("Address"),"No Address");
                    Query orders = firestore.collection("Orders").document(userEmail).collection("Orders").orderBy("TimeOrder", Query.Direction.ASCENDING);
                    orders.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot snapshot) {

                            if(snapshot.isEmpty()){
                                //           Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for(QueryDocumentSnapshot orderSnapshot : snapshot){
                                //In this block of code, will get the Information of the Orders  which will be needed later
                                String orderID = orderSnapshot.get("ID").toString();
                                String orderUserID = orderSnapshot.get("UserID").toString();
                                String orderNo = orderSnapshot.get("OrderNo").toString();
                                String timeOrder = orderSnapshot.get("TimeOrder").toString();
                                String dateOrder = Utils.CheckTextIfNull(orderSnapshot.get("TimeOrderName"),"No Date");
                                String status = orderSnapshot.get("Status").toString();
                                String total = orderSnapshot.get("Total").toString();
                                Log.e("DateNow",Utils.TimeAndDateNow("date"));
                                Log.e("TimeOrder",timeOrder);
                                if(timeOrder.contains(Utils.TimeAndDateNow("date")) || status.equals("InProgress")){

                                    //This condition will be trigger if the current time order of the Order was todate or the status was still in Progress
                                    //  productModels.add(new ProductModel(orderID,new OrderModel(orderID,orderNo,"0","0",total,status),new UserModel(userName,userContact,userAddress,userEmail)));
                                    ProductModel productModel = new ProductModel();
                                    productModel.setID(orderID);
                                    OrderModel orderModel = new OrderModel();
                                    orderModel.setOrderNumber(orderID);
                                    orderModel.setOrderID(orderNo);
                                    orderModel.setSubtotal("0");
                                    orderModel.setShippingFee("0");
                                    orderModel.setGrandTotal(total);
                                    orderModel.setStatus(status);
                                    //orderModel.setDate(timeOrder.replace(" ","-"));
                                    orderModel.setDate(dateOrder);
                                    productModel.setOrderModel(orderModel);
                                    UserModel userModel = new UserModel();
                                    userModel.setName(userName);
                                    userModel.setContact(userContact);
                                    userModel.setAddress(userAddress);
                                    userModel.setEmail(userEmail);
                                    productModel.setUserModel(userModel);

                                    if(orderID != null || orderID != ""){
                                        productModels.add(productModel);
                                    }





                                }

                            }



                            //This will instantiate the recycler view and adapter to be later append on our display
                            RecyclerView productRecyclerView = (RecyclerView)findViewById(R.id.adminPageRecyclerView);
                            AdminOrderRecyclerView adapter = new AdminOrderRecyclerView(getApplicationContext(),productModels,AdminOrderRecyclerView.Page.main);
                            productRecyclerView.setHasFixedSize(true);
                            productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            productRecyclerView.setAdapter(adapter);
                            onDataChanged = true;

                        }
                    });

                }
            }
        });
    }
}