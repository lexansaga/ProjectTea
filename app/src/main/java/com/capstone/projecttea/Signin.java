package com.capstone.projecttea;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    EditText edt_signin_email, edt_signin_password;
    Button btn_signin, btn_createaccount;
    SharedPreferences preferences;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edt_signin_email = findViewById(R.id.edt_signin_email);
        edt_signin_password = findViewById(R.id.edt_signin_password);

        btn_signin = findViewById(R.id.btn_signin_page);
        btn_createaccount = findViewById(R.id.btn_createaccount_page);

        firestore = FirebaseFirestore.getInstance();

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        String currentUser = preferences.getString("User", "").toString();
        if (!currentUser.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
            return;
        }

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),Home.class));
                Query getCredential = firestore.collection("User").whereEqualTo("Email", edt_signin_email.getText().toString()).whereEqualTo("Password", edt_signin_password.getText().toString());
                getCredential.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            SharedPreferences.Editor preferenceEditor = preferences.edit();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                preferenceEditor.putString("User", queryDocumentSnapshot.get("Email").toString());
                            }
                            preferenceEditor.apply();
                            Toast.makeText(getApplicationContext(), "" + preferences.getString("User", "").toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();

                        } else {
                            //Task is not successfull
                        }
                    }
                });
            }
        });

        btn_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Administrator.class));
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}