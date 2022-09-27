package com.capstone.projecttea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {
    TextView productName, productPrice, productDescription;
    ImageView productImage;
    Spinner productVariations, productAddOns;
    EditText quantity;
    ImageButton quantityPlus, quantityMinus;
    ListView listAddOns;
    ImageView userButton, pdCart;
    Button btnAddToCart,btnBuyNow;
    SharedPreferences preferences;
    FirebaseFirestore firestore;
    FirebaseHandler firebaseHandler;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        firestore = FirebaseFirestore.getInstance();
        firebaseHandler = new FirebaseHandler(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        productName = findViewById(R.id.descItemName);
        productPrice = findViewById(R.id.descItemPrice);
        productDescription = findViewById(R.id.descItemDescription);
        productImage = findViewById(R.id.descProductImage);
        productVariations = findViewById(R.id.descSpinnerVariations);
        listAddOns = findViewById(R.id.descSelectedAddOns);
        productAddOns = findViewById(R.id.descAddOns);

        quantity = findViewById(R.id.descQuantityValue);
        quantityPlus = findViewById(R.id.descQuantityPlus);
        quantityMinus = findViewById(R.id.descQuantityMinus);
        pdCart = findViewById(R.id.pdCart);

        btnAddToCart = findViewById(R.id.descAddtoCart);
        btnBuyNow = findViewById(R.id.descBuyNow);
        userButton = findViewById(R.id.productDescUser);
        preferences = getSharedPreferences("User", MODE_PRIVATE);


        ArrayList<ModelAddOns> arrayModelAddOns = new ArrayList<>();
        Bundle extras = getIntent().getExtras();

        String currency = "₱";
        if (extras != null) {
            Query queryAddOns = firestore.collection("Add_Ons");
            firebaseHandler.FillSpinner(productAddOns,queryAddOns);

            String productID = extras.get("productID").toString();
            Query query = firestore.collection("Items").whereEqualTo("ID",productID);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot snapshot: task.getResult()
                         ) {
                        String id = snapshot.get("ID").toString().trim();

                        String defaultImageLink = "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98";
                     //   Log.e("Image Link at PD",snapshot.get("ImageLink").toString());
                        String imageLink = Utils.CheckTextIfNull(snapshot.get("ImageLink"),defaultImageLink);
                        String name = snapshot.get("Name").toString().trim();
                        String price = currency+snapshot.get("Price").toString().trim();
                        String description = snapshot.get("Description").toString().trim();
                        String variations = snapshot.get("Variation").toString().trim();
                        Query queryVariation = firestore.collection("Items").whereEqualTo("Name",name);
                        firebaseHandler.FillSpinner(productVariations,queryVariation);

                        //This block get and append the product information
                        productName.setText(name);
                        productName.setContentDescription("");
                        productPrice.setText(price);
                        productDescription.setText(description);
                        Glide.with(getApplicationContext()).load(imageLink).into(productImage);
                       // productVariations.setSelection(firebaseHandler.GetItemPosition(null,variatons));
                    }
                }
            });


            btnBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //This code will handle item formatting , ID , Name , Price , Variation
                    ArrayList<ProductModel> models = new ArrayList<>();
                    String id = productName.getText().toString()+"|"+productPrice.getText().toString().replace(currency,"");
                    String buyNowName = productName.getText().toString();
                    String buyNowPrice = productPrice.getText().toString().replace(currency,"");
                    String buyNowVariation;
                    if(productVariations.getSelectedItem().toString().contains("Choose")){
                        //User can't choose default , hence we automatically set item 1
                        productVariations.setSelection(1);
                        buyNowVariation = productVariations.getSelectedItem().toString();
                    }
                    else{
                        buyNowVariation = productVariations.getSelectedItem().toString();
                    }
                    //*******************************************************************

                    //This code will handle addOns

                    ArrayList<String> addOnsName = new ArrayList<>();
                    int addOnsPrice = 0;
                    if(!arrayModelAddOns.isEmpty()){

                        for (ModelAddOns modelAddOns: arrayModelAddOns
                        ) {
                            addOnsName.add(modelAddOns.getName()+":"+modelAddOns.getPrice());
                            addOnsPrice += Integer.parseInt(modelAddOns.getPrice());
                        }
                    }
                    //*****************************

                    //Product Model Initiation
                    //We need to save our data on a model before passing on the Checkout Page
                    ProductModel model = new ProductModel();
                    model.setID(id);
                    model.setImageLink(productID);
                    model.setProductName(buyNowName);
                    model.setPrice(buyNowPrice);
                    model.setVariation(buyNowVariation);
                    model.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    model.setAddOns(String.join(",",addOnsName));

                    //********************************************************************

                    //Adding the data on Product Model Arraylist then send it to CheckoutPage Activity
                    models.add(model);

                    Intent intent = new Intent(getApplicationContext(), CheckoutPage.class);
                    intent.putExtra("isBuyNow","true");
                    intent.putExtra("cart",models);
                    startActivity(intent);
                    //********************************************************************************
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
                    //This code will append  the adds on on String to be save on database and compute the total price including the addons
                   ArrayList<String> addOnsName = new ArrayList<>();
                    int addOnsPrice = 0;
                    if(!arrayModelAddOns.isEmpty()){

                        for (ModelAddOns modelAddOns: arrayModelAddOns
                        ) {
                            addOnsName.add(modelAddOns.getName()+":"+modelAddOns.getPrice());
                            addOnsPrice += Integer.parseInt(modelAddOns.getPrice());
                        }
                    }
                    //*******************************************

                    //This code will Insert the data to Cart Collection, First we need to add it on an HashMap before adding
                    String id = firestore.collection("Cart").document().getId();
                    Map<String,Object> data = new HashMap<>();
                    data.put("ID",id);
                    data.put("ProductID",productID);
                    data.put("ProductName",productName.getText().toString());
                    if(addOnsPrice == 0){
                        data.put("ProductPrice",productPrice.getText().toString().replace(currency,""));
                    }
                    else{
                        int itemPrice = addOnsPrice +  Integer.parseInt(productPrice.getText().toString().replace(currency,""));
                        data.put("ProductPrice",itemPrice);
                    }
                    data.put("UserID",user);
                    data.put("Variation",productVariations.getSelectedItem());
                    data.put("Quantity",Integer.parseInt(quantity.getText().toString()));
                    data.put("AddOns",String.join(",",addOnsName));
                    //******************************************

                    //This code will get the downloadUrl of the image so we can insert it on database
                    storageReference.child("Items/"+productID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            data.put("ImageLink",uri.toString());

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
                    //********************************************



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


        userButton.setOnClickListener(new View.OnClickListener() {
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
                            case R.id.user_logout:
                                preferences.edit().remove("User").apply();
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


        pdCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Cart.class));
            }
        });


        productAddOns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedAddOns = adapterView.getItemAtPosition(position).toString();

              //  Toast.makeText(getApplicationContext(), selectedAddOns, Toast.LENGTH_SHORT).show();

                Query query = FirebaseFirestore.getInstance().collection("Add_Ons").whereEqualTo("Name",selectedAddOns);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if(snapshots.isEmpty()){
                           return;
                        }

                        for (QueryDocumentSnapshot snapshot: snapshots
                             ) {
                                String name = snapshot.get("Name").toString();
                                String price = snapshot.get("Price").toString();
                       //     Toast.makeText(getApplicationContext(), name + price, Toast.LENGTH_SHORT).show();
                            ModelAddOns modelAddOns = new ModelAddOns();
                            modelAddOns.setName(name);
                            modelAddOns.setPrice(price);
                            arrayModelAddOns.add(modelAddOns);
                        }

                        AdapterAddOns adapterAddOns = new AdapterAddOns(getApplicationContext(),arrayModelAddOns);
                        listAddOns.setAdapter(adapterAddOns);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}