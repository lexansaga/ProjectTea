package com.capstone.projecttea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {
    TextView productName, productPrice, productDescription;
    ImageView productImage;
    Spinner productVariations;
    EditText quantity;
    ImageButton quantityPlus, quantityMinus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        productName = findViewById(R.id.descItemName);
        productPrice = findViewById(R.id.descItemPrice);
        productDescription = findViewById(R.id.descItemDescription);
        productImage = findViewById(R.id.descProductImage);
        productVariations = findViewById(R.id.descSpinnerVariations);
        quantity = findViewById(R.id.descQuantityValue);
        quantityPlus = findViewById(R.id.descQuantityPlus);
        quantityMinus = findViewById(R.id.descQuantityMinus);

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