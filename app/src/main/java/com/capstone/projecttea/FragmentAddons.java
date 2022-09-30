package com.capstone.projecttea;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddons extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnAdd, btnUpdate, btnDelete, btnSave;
    CardView cardSelectAddOns;
    EditText edtAddOnsName, edtAddOnsPrice;
    Spinner spinAddOns;

    String selectedFunction;
    FirebaseFirestore firestore;
    Context context;
    FirebaseHandler firebaseHandler;

    public FragmentAddons() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddons.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddons newInstance(String param1, String param2) {
        FragmentAddons fragment = new FragmentAddons();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        firestore = FirebaseFirestore.getInstance();
        firebaseHandler = new FirebaseHandler(context);
        selectedFunction = "Add";
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addons, container, false);
        btnAdd = view.findViewById(R.id.btnAddOnsAdd);
        btnUpdate = view.findViewById(R.id.btnAddOnsUpdate);
        btnDelete = view.findViewById(R.id.btnAddOnsDelete);
        btnSave = view.findViewById(R.id.btnAddOnsSave);

        cardSelectAddOns = view.findViewById(R.id.cardSelectAddOns);
        edtAddOnsName = view.findViewById(R.id.edtAddOnsName);
        edtAddOnsPrice = view.findViewById(R.id.edtAddOnsPrice);

        spinAddOns = view.findViewById(R.id.spinnerAddOnsSelect);

        setSelected(btnAdd, new Button[]{btnUpdate, btnDelete}, "Add");
        setSelected(btnUpdate, new Button[]{btnAdd, btnDelete}, "Update");
        setSelected(btnDelete, new Button[]{btnAdd, btnUpdate}, "Delete");
        firebaseHandler.FillSpinner(spinAddOns, firestore.collection("Add_Ons"));
        firestore = FirebaseFirestore.getInstance();
        spinAddOns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                firestore.collection("Add_Ons").document(spinAddOns.getSelectedItem().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                String name = snapshot.get("Name").toString();
                                String price = snapshot.get("Price").toString();
                                edtAddOnsName.setText(name);
                                edtAddOnsPrice.setText(price);
                            }
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFunction.equals("Delete")) {
                    if (spinAddOns.getSelectedItem().toString().contains("Choose")) {
                        Toast.makeText(context, "Select " + spinAddOns.getContentDescription() + " first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete this Add Ons?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            firestore.collection("Add_Ons").document(spinAddOns.getSelectedItem().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Add Ons deleted Sucessfully", Toast.LENGTH_SHORT).show();
                                    ResetDefault();
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.font_color));
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.font_color));
                    return;
                }
                if (edtAddOnsName.getText().toString().isEmpty() || edtAddOnsPrice.getText().toString().isEmpty()) {

                    Toast.makeText(getContext(), "Please fill up AddOns Name and Price", Toast.LENGTH_SHORT).show();

                    return;
                }


                //Default for Add and Update
                Map<String, Object> data = new HashMap<>();
                data.put("Name", edtAddOnsName.getText().toString().trim());
                data.put("Price", edtAddOnsPrice.getText().toString().trim());

                firestore.collection("Add_Ons").document(edtAddOnsName.getText().toString()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseHandler.FillSpinner(spinAddOns, firestore.collection("Add_Ons"));
                        ResetDefault();
                        Toast.makeText(context, "Add Ons Added Successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });

        return view;
    }

    void ResetDefault() {
        spinAddOns.setSelection(0);
        edtAddOnsName.setText("");
        edtAddOnsPrice.setText("");
    }

    void setSelected(Button button, Button[] arr, String selected) {

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selectedFunction = selected;
                button.setBackground(getResources().getDrawable(R.drawable.btn_radioselected, null));
                button.setTextColor(getResources().getColor(R.color.white, null));
                for (Button btn : arr
                ) {
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_radiodefault, null));
                    btn.setTextColor(getResources().getColor(R.color.primary, null));
                }
                btnSave.setText(selectedFunction + " Add Ons");
                if (selectedFunction.equals("Add")) {
                    cardSelectAddOns.setVisibility(View.GONE);
                } else {
                    cardSelectAddOns.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}