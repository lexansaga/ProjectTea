package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_signin, btn_createaccount;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //  startActivity(new Intent(getApplicationContext(),Administrator.class));
        btn_signin = findViewById(R.id.btn_signin);
        btn_createaccount = findViewById(R.id.btn_createaccount);

        preferences = getSharedPreferences("User", MODE_PRIVATE);
       // SharedPreferences.Editor preferenceEditor = preferences.edit();
        String currentUser = preferences.getString("User", "").toString();
        String accountType = preferences.getString("AccountType", "").toString();
        if (!currentUser.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
            return;
        }
        if(!accountType.isEmpty()){
            startActivity(new Intent(getApplicationContext(), Administrator.class));
            finish();
            return;
        }

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Signin.class));
            }
        });

        btn_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CreateAccount.class));
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}