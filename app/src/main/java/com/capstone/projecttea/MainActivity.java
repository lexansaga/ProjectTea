package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_signin, btn_createaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      // startActivity(new Intent(getApplicationContext(),Home.class));
        btn_signin = findViewById(R.id.btn_signin);
        btn_createaccount = findViewById(R.id.btn_createaccount);

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