package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class User extends AppCompatActivity {
    EditText edtFullName,edtContactNo,edtAddress,edtUsername,edtCurrentPass,edtNewPassword;
    Button btnSave;
    ImageView btnUser,btnCart;
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

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.user_information:
                                startActivity(new Intent(getApplicationContext(),User.class));
                                return true;
                            case R.id.user_history:
                                startActivity(new Intent(getApplicationContext(),AdminHistory.class));
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
                startActivity(new Intent(getApplicationContext(),Cart.class));
            }
        });


    }
}