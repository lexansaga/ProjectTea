package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {
    EditText edtFullName, edtContactNo, edtAddress, edtUsername, edtCurrentPass, edtNewPassword;
    Button btnSave;
    ImageView btnUser, btnCart;
    FirebaseFirestore firestore;
    SharedPreferences preferences;
    String userKey = "", userPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtFullName = findViewById(R.id.edtUserFullName);
        edtContactNo = findViewById(R.id.edtUserContact);
        edtAddress = findViewById(R.id.edtUserAddress);
        edtUsername = findViewById(R.id.edtUserUsername);
        edtCurrentPass = findViewById(R.id.edtUserCurrentPassword);
        edtNewPassword = findViewById(R.id.edtUserNewPassword);

        btnSave = findViewById(R.id.btnSaveUser);


        btnUser = findViewById(R.id.userUser);
        btnCart = findViewById(R.id.userCart);

        firestore = FirebaseFirestore.getInstance();

        preferences = getSharedPreferences("User", MODE_PRIVATE);
        // SharedPreferences.Editor preferenceEditor = preferences.edit();
        String currentUser = preferences.getString("User", "").toString();
        Query getUserInfo = firestore.collection("User").whereEqualTo("Email", currentUser);
        LoadUser(getUserInfo);

        btnUser.setOnClickListener(new View.OnClickListener() {
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
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("User Password",userPassword);
                Log.e("User Key",userKey);
                Map<String, Object> data = new HashMap<>();
                if(!edtFullName.getText().toString().trim().equals("")){
                    data.put("Fullname", edtFullName.getText().toString().trim());
                }
                if(!edtContactNo.getText().toString().trim().equals("")){
                    data.put("Contact", edtContactNo.getText().toString().trim());
                }
                if(!edtAddress.getText().toString().trim().equals("")){
                    data.put("Address", edtAddress.getText().toString().trim());
                }
                if(!edtUsername.getText().toString().trim().equals("")){
                    data.put("Username", edtUsername.getText().toString().trim());
                }
                if(!edtNewPassword.getText().toString().trim().equals("")){
                    data.put("Password", edtNewPassword.getText().toString().trim());
                }

                if (userPassword.equals(edtNewPassword.getText().toString().trim())) {
                    firestore.collection("User").document(userKey).set(data, SetOptions.merge());

                    Toast.makeText(getApplicationContext(), "Data Update Successfully", Toast.LENGTH_SHORT).show();
                    ResetDefault();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Password Not Equal!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void LoadUser(Query query) {

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot snapshot : snapshots) {
                    String userName = snapshot.get("Fullname").toString().trim();
                    String userContact = snapshot.get("Contact").toString().trim();
                    String userAddress = snapshot.get("Address").toString().trim();
                    String userEmail = snapshot.get("Email").toString().trim();
                    String username = snapshot.get("Username").toString().trim();
                    String password = snapshot.get("Password").toString().trim();
                    edtFullName.setHint("Fullname (" + userName + ")");
                    edtContactNo.setHint("Contact Number (" + userContact + ")");
                    edtAddress.setHint("Address (" + userAddress + ")");
                    edtUsername.setHint("Username (" + username + ")");
                    userKey = snapshot.getId();
                    userPassword = password;

                    // edtUsername.setHint("Fullname ("+userName+")");
                    //   Toast.makeText(CheckoutPage.this, ""+userContact, Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(getApplicationContext(), ""+userName, Toast.LENGTH_SHORT).show();
                    //     txtContact.setText(userContact);
                    //    txtAddress.setText(userAddress);
                }

            }
        });
    }

    private  void ResetDefault(){
        edtFullName.setText("");
        edtContactNo.setText("");
        edtAddress.setText("");
        edtUsername.setText("");
        edtCurrentPass.setText("");
        edtNewPassword.setText("");
    }
}