package com.capstone.projecttea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {
    TextView productName, productPrice, productDescription;
    ImageView productImage;
    Spinner productVariations;
    EditText quantity;
    ImageButton quantityPlus, quantityMinus;

    Button btnAddToCart,btnBuyNow;

    FirebaseFirestore firestore;
    FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        firestore = FirebaseFirestore.getInstance();
        firebaseHandler = new FirebaseHandler(this);
        productName = findViewById(R.id.descItemName);
        productPrice = findViewById(R.id.descItemPrice);
        productDescription = findViewById(R.id.descItemDescription);
        productImage = findViewById(R.id.descProductImage);
        productVariations = findViewById(R.id.descSpinnerVariations);
        quantity = findViewById(R.id.descQuantityValue);
        quantityPlus = findViewById(R.id.descQuantityPlus);
        quantityMinus = findViewById(R.id.descQuantityMinus);

        btnAddToCart = findViewById(R.id.descAddtoCart);
        btnBuyNow = findViewById(R.id.descBuyNow);

        Bundle extras = getIntent().getExtras();

        String currency = "₱";
        if (extras != null) {
            String productID = extras.get("productID").toString();
            Query query = firestore.collection("Items").whereEqualTo("ID",productID);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot: task.getResult()
                         ) {
                        String imageLink = snapshot.get("ImageLink").toString().trim();
                        String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";
                        String name = snapshot.get("Name").toString().trim();
                        String price = currency+snapshot.get("Price").toString().trim();
                        String description = snapshot.get("Description").toString().trim();
                        String variations = snapshot.get("Variation").toString().trim();
                        Query queryVariation = firestore.collection("Items").whereEqualTo("Name",name);
                        firebaseHandler.FillSpinner(productVariations,queryVariation);

                        productName.setText(name);
                        productPrice.setText(price);
                        productDescription.setText(description);
                        Glide.with(getApplicationContext()).load(imageLink.equals("") ? defaultImageLink : imageLink).into(productImage);
                       // productVariations.setSelection(firebaseHandler.GetItemPosition(null,variatons));
                    }
                }
            });


            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = getSharedPreferences("User",MODE_PRIVATE);
                    String user =  preferences.getString("User","").toString();
                    if(user.equals("")){
                        //No user login
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Please login first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String id = firestore.collection("Cart").document().getId();
                    Map<String,Object> data = new HashMap<>();
                    data.put("ID",id);
                    data.put("ProductID",productID);
                    data.put("ProductName",productName.getText().toString());
                    data.put("ProductPrice",productPrice.getText().toString().replace(currency,""));
                    data.put("UserID",user);
                    data.put("Quantity",Integer.parseInt(quantity.getText().toString()));
                    data.put("AddOns","AddOns1,AddOns2");


                    firestore.collection("Cart").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), productName.getText().toString()+" added to cart!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }





        quantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityVal = quantity.getText().toString();
                if (!quantityVal.isEmpty()) {
                    int quantityValue = Integer.parseInt(quantityVal);
                    if (quantityValue < 999) {
                        quantityValue++;
                        quantity.setText("" + quantityValue);
                        //  Toast.makeText(getApplicationContext(), "Plus " + quantityValue, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        quantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityVal = quantity.getText().toString();
                if (!quantityVal.isEmpty()) {
                    int quantityValue = Integer.parseInt(quantityVal);
                    if (quantityValue > 0) {
                        quantityValue--;
                        quantity.setText("" + quantityValue);
                    }
                }
                // Toast.makeText(getApplicationContext(), "Minus", Toast.LENGTH_SHORT).show();
            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String quantityVal = quantity.getText().toString();
                if (!quantityVal.isEmpty()) {
                    int quantityValue = Integer.parseInt(quantityVal);
                    if (quantityValue < 0 || quantityValue > 999) {
                        quantity.setText("" + 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}