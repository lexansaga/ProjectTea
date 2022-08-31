package com.capstone.projecttea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    EditText edt_signin_email,edt_signin_password;
    Button btn_signin,btn_createaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edt_signin_email = findViewById(R.id.edt_signin_email);
        edt_signin_password = findViewById(R.id.edt_signin_password);

        btn_signin = findViewById(R.id.btn_signin_page);
        btn_createaccount = findViewById(R.id.btn_createaccount_page);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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