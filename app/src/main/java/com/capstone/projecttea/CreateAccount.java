package com.capstone.projecttea;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    EditText edtFullName ,edtUsername,edtEmail,edtPassword,edtConfirmPassword;
    Button btnCreateAccount, btnSignin;

    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        edtFullName = findViewById(R.id.edt_ca_fullname);
        edtUsername = findViewById(R.id.edt_ca_username);
        edtEmail = findViewById(R.id.edt_ca_email);
        edtPassword = findViewById(R.id.edt_ca_password);
        edtConfirmPassword = findViewById(R.id.edt_ca_confirmpass);

        btnCreateAccount = findViewById(R.id.btn_ca_createaccount);
        btnSignin = findViewById(R.id.btn_ca_signin);

        firestore = FirebaseFirestore.getInstance();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Signin.class));
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Toast.makeText(getApplicationContext(),"Create Account",Toast.LENGTH_SHORT).show();
              if(isTextEmpty(edtFullName) || isTextEmpty(edtUsername)|| isTextEmpty(edtEmail)|| isTextEmpty(edtPassword)|| isTextEmpty(edtConfirmPassword)){
                  Toast.makeText(getApplicationContext(),"Please fill up all information!",Toast.LENGTH_SHORT).show();
                  return;
              }
            if(!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                Toast.makeText(getApplicationContext(),"Password not Match!" ,Toast.LENGTH_SHORT).show();
                return;
            }

                Query emailAlreadyExists = firestore.collection("User").whereEqualTo("Email",edtEmail.getText().toString());
                emailAlreadyExists.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            if(!task.getResult().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Email already exists!" ,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Map<String,Object> data = new HashMap<>();
                            data.put("Email",edtEmail.getText().toString().trim());
                            data.put("Username",edtUsername.getText().toString().trim());
                            data.put("Password",edtPassword.getText().toString().trim());
                            data.put("Fullname",edtFullName.getText().toString().trim());
                            data.put("Address","");
                            data.put("Contact","");
                            firestore.collection("User").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    resetDefault();
                                    startActivity(new Intent(getApplicationContext(),Signin.class));
                                    Toast.makeText(getApplicationContext(),"Account Created Successfully",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                          // If task is unsuccessfull!
                        }

                    }
                });
            }


        });
    }

    boolean isTextEmpty(EditText editText){
        return editText.getText().toString().equals("");
    }


    void resetDefault(){
        edtEmail.setText("");
        edtUsername.setText("");
        edtPassword.setText("");
        edtFullName.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
    }
}